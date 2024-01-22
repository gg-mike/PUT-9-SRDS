package pl.put.srdsproject.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.put.srdsproject.fulfilled.FulfilledService;
import pl.put.srdsproject.inventory.Inventory;
import pl.put.srdsproject.inventory.InventoryService;
import pl.put.srdsproject.util.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final FulfilledService fulfilledService;
    private final InventoryService inventoryService;

    @Value("${application.id}")
    private String applicationId;

    @Value("${cassandra.update-delay}")
    private Long cassandraUpdateDelay;

    @Value("${cassandra.max-iterations}")
    private int maxIterations;

    public Request addRequest(Request request) {
        return requestRepository.save(request);
    }

    public Map<String, LongSummaryStatistics> getReport() {
        return requestRepository.getReport()
                .stream().map(request -> new RequestReport(request.getProductId(), request.getQuantity()))
                .collect(groupingBy(RequestReport::productId, summarizingLong(RequestReport::quantity)));
    }


    @Scheduled(fixedRate = 10_000)
    public void processNewRequests() {
        var notClaimedRequests = requestRepository.findNotClaimedRequests();
        notClaimedRequests.forEach(this::claimRequest);

        // wait for Cassandra to update
        waitForCassandraUpdate();

        var claimedRequests = requestRepository.findClaimedRequests(applicationId); //will also return all claimed during which the system failed
        processRequests(claimedRequests);

        requestRepository.deleteProcessedRequests(claimedRequests.stream().map(Request::getId).toList());
    }


    private void processRequests(List<Request> claimedRequests) {
        for (var request : claimedRequests) {
            processRequest(request);
        }
    }

    private void processRequest(Request request) {
        log.info("Processing request {}", request.getId());
        // check if request was already fulfilled (crush happened between fulfilled added and request deleted)
        try {
            fulfilledService.getFulfilled(request.getId());
            requestRepository.deleteById(request.getId());
            return;
        } catch(NotFoundException ignored) {}

        var previouslyClaimedProducts = inventoryService.findProductsByHandlerIdAndRequestId(applicationId, request.getId()); // if request processing failed while some products were claimed already
        log.info("Found {} previously claimed products for request {}", previouslyClaimedProducts.size(), request.getId());
        List<Inventory> collectedProducts = new ArrayList<>(previouslyClaimedProducts);

        for (int i = 0; i < maxIterations && collectedProducts.size() < request.getQuantity(); i++) {
            var availableProducts = inventoryService.findAvailableProductsByProductIdAndMinimumQuantity(request.getProductId(), request.getQuantity() - collectedProducts.size());
            log.info("Found {} available products for request {}", availableProducts.size(), request.getId());
            if (i == 0 && availableProducts.size() < request.getQuantity()) {
                //not enough products available at all
                break;
            }

            for (var product : availableProducts) {
                product.setRequestId(request.getId());
                product.setHandlerId(applicationId);
            }

            inventoryService.saveAll(availableProducts);
            waitForCassandraUpdate();

            var updatedProducts = inventoryService.findProductsByHandlerIdAndRequestId(applicationId, request.getId());
            if (updatedProducts.size() < request.getQuantity()) {
                log.warn("Conflict occurred while processing request {} (updated: {}, requested: {})",
                        request.getId(), updatedProducts.size(), request.getQuantity());
            }

            collectedProducts.addAll(updatedProducts);
        }

        if (collectedProducts.size() != request.getQuantity()) {
            //not enough products available - request marked as failed
            for (var product : collectedProducts) {
                product.setHandlerId("");
                product.setRequestId("");
            }
            inventoryService.saveAll(collectedProducts);
            request.setQuantity(-1L);
            log.warn("Could not fulfill request: {} (collected: {}, requested: {})",
                    request.getId(), collectedProducts.size(), request.getQuantity());
        }

        fulfilledService.addFulfilled(request);
        requestRepository.deleteById(request.getId());
    }


    private void waitForCassandraUpdate() {
        try {
            Thread.sleep(cassandraUpdateDelay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void claimRequest(Request request) {
        request.setApplicationId(applicationId);
        requestRepository.save(request);
    }


}

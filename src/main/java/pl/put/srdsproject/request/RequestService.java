package pl.put.srdsproject.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.put.srdsproject.inventory.Inventory;
import pl.put.srdsproject.inventory.InventoryService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final InventoryService inventoryService;

    @Value("${application.id}")
    private String applicationId;

    @Value("${cassandra.update-delay}")
    private Long cassandraUpdateDelay;

    @Value("${cassandra.max-iterations}")
    private int maxIterations;

    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    public Request addRequest(Request request) {
        return requestRepository.save(request);
    }

    public List<RequestReport> getReport() {
        return requestRepository.getReport()
                .stream().map(request -> new RequestReport(request.getProductId(), request.getQuantity()))
                .toList();
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
        var previouslyClaimedProducts = inventoryService.findProductsByHandlerIdAndRequestId(applicationId, request.getId()); // if request processing failed while some products were claimed already
        List<Inventory> collectedProducts = new ArrayList<>(previouslyClaimedProducts);

        for (int i = 0; i < maxIterations; i++) {
            var availableProducts = inventoryService.findAvailableProductsByProductIdAndMinimumQuantity(request.getProductId(), request.getQuantity() - collectedProducts.size());
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
                log.warn("Conflict occurred while processing request {}", request.getId());
            }

            collectedProducts.addAll(updatedProducts);
        }

        if (collectedProducts.size() != request.getQuantity()) {
            //not enough products available - request marked as failed
            for (var product : collectedProducts) {
                product.setHandlerId(null);
                product.setRequestId(null);
            }
            inventoryService.saveAll(collectedProducts);
            log.warn("Could not fulfill request: {}", request.getId());
        }

        //todo add to fulfilled

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

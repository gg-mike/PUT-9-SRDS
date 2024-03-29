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

import java.util.*;

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

    @Value("${cassandra.max-fetch-requests}")
    private int maxFetchRequests;

    public RequestReport getReport() {
        var all = requestRepository.findAll();
        var claimed  = all.stream().filter(fulfilled -> !Objects.equals(fulfilled.getApplicationId(), "none")).toList();
        return new RequestReport(
                all.size() - claimed.size(),
                claimed.size(),
                all.stream().collect(groupingBy(Request::getProductId, summingLong(Request::getQuantity)))
        );
    }

    public List<Request> getAll() {
        return requestRepository.findAll();
    }

    public Request getById(String id) {
        return requestRepository.findById(id).orElseThrow(NotFoundException::new);
    }
    public Request add(Request request) {
        return requestRepository.save(request);
    }

    public void deleteAll() {
        requestRepository.deleteAll();
    }


    @Scheduled(fixedDelay = 10_000)
    public void processNewRequests() {
        var notClaimedRequests = requestRepository.findNotClaimedRequests(maxFetchRequests);
        notClaimedRequests.forEach(this::claimRequest);

        // wait for Cassandra to update
        waitForCassandraUpdate();

        var claimedRequests = requestRepository.findClaimedRequests(applicationId); //will also return all claimed during which the system failed
        processRequests(claimedRequests);

        requestRepository.deleteAll(claimedRequests);
    }


    private void processRequests(List<Request> claimedRequests) {
        for (var request : claimedRequests) {
            processRequest(request);
        }
    }

    private void processRequest(Request request) {
        log.info("Processing request {}", request.getId());
        // check if request was already fulfilled (crash happened between fulfilled added and request deleted)
        try {
            fulfilledService.getById(request.getId());
            requestRepository.deleteById(request.getId());
            return;
        } catch(NotFoundException ignored) {}

        var previouslyClaimedProducts = inventoryService.findProductsByHandlerIdAndRequestId(applicationId, request.getId()); // if request processing failed while some products were claimed already
        if (!previouslyClaimedProducts.isEmpty()) {
            log.info("Found {} previously claimed products for request {}", previouslyClaimedProducts.size(), request.getId());
        }

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

        boolean isSuccessful = true;
        if (collectedProducts.size() != request.getQuantity()) {
            //not enough products available - request marked as failed
            for (var product : collectedProducts) {
                product.setHandlerId("none");
                product.setRequestId("none");
            }
            inventoryService.saveAll(collectedProducts);
            log.warn("Could not fulfill request: {} (collected: {}, requested: {})",
                    request.getId(), collectedProducts.size(), request.getQuantity());
            isSuccessful = false;
        }

        if(collectedProducts.size() == request.getQuantity()) {
            log.info("Fulfilled request: {}", request.getId());
        }

        fulfilledService.add(request, isSuccessful);
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

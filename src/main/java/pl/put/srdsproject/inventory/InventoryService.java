package pl.put.srdsproject.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingLong;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public List<Inventory> addProduct(String productId, Long quantity) {
        var products = new ArrayList<Inventory>();
        for (int i = 0; i < quantity; i++) {
            products.add(new Inventory(new InventoryKey(UUID.randomUUID().toString(), productId), "", ""));
        }

        return inventoryRepository.saveAll(products);
    }

    public List<Inventory> findAvailableProductsByProductIdAndMinimumQuantity(String productId, Long quantity) {
        if (quantity <= 0L) return Collections.emptyList();
        return inventoryRepository.findAvailableProductsByProductId(productId, quantity.intValue());
    }

    public void saveAll(List<Inventory> products) {
        inventoryRepository.saveAll(products);
    }

    public List<Inventory> findProductsByHandlerIdAndRequestId(String applicationId, String requestId) {
        return inventoryRepository.findProductsByHandlerIdAndRequestId(applicationId, requestId);
    }

    public Map<String, LongSummaryStatistics> getReport() {
        return inventoryRepository.getReport()
                .stream().map(fulfilled -> new InventoryReport(fulfilled.getId().getProductId(), 1L))
                .collect(groupingBy(InventoryReport::productId, summarizingLong(InventoryReport::quantity)));
    }
}

package pl.put.srdsproject.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
        return inventoryRepository.findAvailableProductsByProductId(productId, quantity);
    }

    public void saveAll(List<Inventory> products) {
        inventoryRepository.saveAll(products);
    }

    public List<Inventory> findProductsByHandlerIdAndRequestId(String applicationId, String requestId) {
        return inventoryRepository.findProductsByHandlerIdAndRequestId(applicationId, requestId);
    }

    public List<InventoryReport> getReport() {
        return Collections.emptyList();
    }
}

package pl.put.srdsproject.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public List<Inventory> add(String productId, Long quantity) {
        var products = new ArrayList<Inventory>();
        for (int i = 0; i < quantity; i++) {
            products.add(new Inventory(UUID.randomUUID().toString(), productId, "", ""));
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

    public InventoryReport getReport() {
        var all = inventoryRepository.findAll();

        return new InventoryReport(
                all.stream()
                        .filter(inventory -> Objects.equals(inventory.getRequestId(), ""))
                        .collect(groupingBy(Inventory::getProductId, counting())),
                all.stream()
                        .filter(inventory -> !Objects.equals(inventory.getRequestId(), ""))
                        .collect(groupingBy(Inventory::getProductId, counting())),
                all.stream().collect(groupingBy(Inventory::getProductId, counting()))
        );
    }

    public void deleteAll() {
        inventoryRepository.deleteAll();
    }
}

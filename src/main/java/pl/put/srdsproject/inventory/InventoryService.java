package pl.put.srdsproject.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.put.srdsproject.util.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public Inventory removeProducts(InventoryKey inventoryKey, Long numberToRemove) {
        var inventory = inventoryRepository.findById(inventoryKey).orElseThrow(NotFoundException::new);
        if (inventory.getQuantity() < numberToRemove) {
            throw new InvalidQuantityException(inventory, numberToRemove);
        }

        inventory.setQuantity(inventory.getQuantity() - numberToRemove);
        return inventoryRepository.save(inventory);
    }

    public Inventory addProducts(InventoryKey inventoryKey, Long numberToAdd) {
        var inventory = inventoryRepository.findById(inventoryKey).orElse(new Inventory(inventoryKey, numberToAdd));
        inventory.setQuantity(inventory.getQuantity() + numberToAdd);
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }
}

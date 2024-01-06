package pl.put.srdsproject.inventory;

import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import pl.put.srdsproject.util.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    public Set<Inventory> moveProducts(InventoryKey sourceKey, InventoryKey targetKey, Long numberToMove) {
        if (Objects.equals(sourceKey.getProductId(), targetKey.getProductId())) {
            throw new IllegalStateException();
        }

        var source = inventoryRepository.findById(sourceKey).orElseThrow(NotFoundException::new);
        var target = inventoryRepository.findById(targetKey).orElseThrow(NotFoundException::new);

        if (source.getQuantity() < numberToMove) {
            throw new InvalidQuantityException(source, numberToMove);
        }

        source.setQuantity(source.getQuantity() - numberToMove);
        target.setQuantity(target.getQuantity() + numberToMove);

        return Set.of(
                inventoryRepository.save(source),
                inventoryRepository.save(target));
    }
}

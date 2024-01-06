package pl.put.srdsproject.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<Inventory> findAll() {
        return inventoryService.findAll();
    }

    @PostMapping
    public Inventory addProducts(@RequestBody InventoryOperation operation) {
        return inventoryService.addProducts(operation.sourceInventoryKey(), operation.numberOfItems());
    }

    @DeleteMapping
    public Inventory removeProducts(@RequestBody InventoryOperation operation) {
        return inventoryService.removeProducts(operation.sourceInventoryKey(), operation.numberOfItems());
    }

    @PutMapping
    public Set<Inventory> moveProducts(@RequestBody InventoryOperation operation) {
        return inventoryService.moveProducts(operation.sourceInventoryKey(), operation.targetInventoryKey(), operation.numberOfItems());
    }


}

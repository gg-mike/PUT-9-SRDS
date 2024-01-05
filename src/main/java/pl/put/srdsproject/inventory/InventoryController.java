package pl.put.srdsproject.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return inventoryService.addProducts(operation.inventoryKey(), operation.numberOfItems());
    }

    @DeleteMapping
    public Inventory removeProducts(@RequestBody InventoryOperation operation) {
        return inventoryService.removeProducts(operation.inventoryKey(), operation.numberOfItems());
    }


}

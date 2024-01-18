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
    public List<InventoryReport> getReport() {
        return inventoryService.getReport();
    }

    @PostMapping
    public List<Inventory> addProducts(@RequestBody InventoryOperation operation) {
        return inventoryService.addProduct(operation.productId(), operation.quantity());
    }
}

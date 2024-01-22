package pl.put.srdsproject.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.LongSummaryStatistics;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public Map<String, LongSummaryStatistics> getReport() {
        return inventoryService.getReport();
    }

    @PostMapping
    public Pair<String, Integer> addProducts(@RequestBody InventoryOperation operation) {
        return Pair.of(operation.productId(), inventoryService.addProduct(operation.productId(), operation.quantity()).size());
    }
}

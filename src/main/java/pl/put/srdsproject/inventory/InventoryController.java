package pl.put.srdsproject.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    public Pair<String, Integer> add(@RequestBody InventoryOperation operation) {
        return Pair.of(operation.productId(), inventoryService.add(operation.productId(), operation.quantity()).size());
    }
}

package pl.put.srdsproject.util;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.put.srdsproject.fulfilled.FulfilledService;
import pl.put.srdsproject.inventory.InventoryService;
import pl.put.srdsproject.request.RequestService;

import java.util.Map;

@RestController
@RequestMapping("/setup")
@RequiredArgsConstructor
public class SetupController {
    private final FulfilledService fulfilledService;
    private final InventoryService inventoryService;
    private final RequestService requestService;

    @PostMapping
    public void populateInventory(@RequestBody Map<String, Long> products) {
        products.forEach(inventoryService::add);
    }

    @DeleteMapping
    public void deleteAll() {
        fulfilledService.deleteAll();
        inventoryService.deleteAll();
        requestService.deleteAll();
    }
}

package pl.put.srdsproject.report;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.put.srdsproject.fulfilled.FulfilledService;
import pl.put.srdsproject.inventory.InventoryService;
import pl.put.srdsproject.request.RequestService;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final FulfilledService fulfilledService;
    private final InventoryService inventoryService;
    private final RequestService requestService;
    @GetMapping
    public Report getReport() {
        return new Report(
                fulfilledService.getReport(),
                inventoryService.getReport(),
                requestService.getReport()
        );
    }
}

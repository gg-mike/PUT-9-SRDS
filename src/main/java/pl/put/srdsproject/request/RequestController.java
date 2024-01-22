package pl.put.srdsproject.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public Map<String, LongSummaryStatistics> getReport() {
        return requestService.getReport();
    }

    @PostMapping
    public Request addRequest(@RequestBody RequestOperation operation) {
        var request = new Request(
                UUID.randomUUID().toString(),
                operation.productId(),
                operation.quantity(),
                ""
        );
        return requestService.addRequest(request);
    }
}

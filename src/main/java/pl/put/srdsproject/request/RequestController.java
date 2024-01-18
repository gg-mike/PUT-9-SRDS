package pl.put.srdsproject.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<RequestReport> getReport() {
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

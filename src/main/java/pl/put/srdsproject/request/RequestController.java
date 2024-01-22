package pl.put.srdsproject.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<Request> getAll() {
        return requestService.getAll();
    }

    @PostMapping
    public Request add(@RequestBody RequestOperation operation) {
        var request = new Request(
                UUID.randomUUID().toString(),
                operation.productId(),
                operation.quantity(),
                ""
        );
        return requestService.add(request);
    }

    @GetMapping("/{id}")
    public Request getById(@PathVariable("id") String id) {
        return requestService.getById(id);
    }
}

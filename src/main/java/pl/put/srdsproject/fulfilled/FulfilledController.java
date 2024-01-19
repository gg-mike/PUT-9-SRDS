package pl.put.srdsproject.fulfilled;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fulfilled")
@RequiredArgsConstructor
public class FulfilledController {
    private final FulfilledService fulfilledService;

    @GetMapping
    public List<FulfilledReport> getReport() {
        return fulfilledService.getReport();
    }

    @GetMapping("/{id}")
    public Fulfilled getFulfilled(@PathVariable("id") String id) {
        return fulfilledService.getFulfilled(id);
    }
}

package pl.put.srdsproject.fulfilled;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.put.srdsproject.request.Request;
import pl.put.srdsproject.util.NotFoundException;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class FulfilledService {
    private final FulfilledRepository fulfillmentRepository;

    public FulfilledReport getReport() {
        var all = fulfillmentRepository.findAll();
        var succeeded  = all.stream().filter(fulfilled -> fulfilled.getQuantity() != -1).toList();
        return new FulfilledReport(
                all.size() - succeeded.size(),
                succeeded.size(),
                succeeded.stream().collect(groupingBy(Fulfilled::getProductId, summingLong(Fulfilled::getQuantity)))
        );
    }

    public List<Fulfilled> getAll() {
        return fulfillmentRepository.findAll();
    }

    public Fulfilled getById(String id) {
        return fulfillmentRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void add(Request request) {
        fulfillmentRepository.save(new Fulfilled(request));
    }

    public void deleteAll() {
        fulfillmentRepository.deleteAll();
    }
}

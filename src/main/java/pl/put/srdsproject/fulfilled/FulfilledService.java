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
        var failed = all.stream().filter(fulfilled -> !fulfilled.isSuccessful()).toList();
        var succeeded  = all.stream().filter(Fulfilled::isSuccessful).toList();
        return new FulfilledReport(
                failed.size(),
                succeeded.size(),
                failed.stream().collect(groupingBy(Fulfilled::getProductId, summingLong(Fulfilled::getQuantity))),
                succeeded.stream().collect(groupingBy(Fulfilled::getProductId, summingLong(Fulfilled::getQuantity)))
        );
    }

    public List<Fulfilled> getAll() {
        return fulfillmentRepository.findAll();
    }

    public Fulfilled getById(String id) {
        return fulfillmentRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void add(Request request, boolean isSuccessful) {
        fulfillmentRepository.save(new Fulfilled(request, isSuccessful));
    }

    public void deleteAll() {
        fulfillmentRepository.deleteAll();
    }
}

package pl.put.srdsproject.fulfilled;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.put.srdsproject.request.Request;
import pl.put.srdsproject.util.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FulfilledService {
    private final FulfilledRepository fulfillmentRepository;

    public List<FulfilledReport> getReport() {
        return fulfillmentRepository.getReport()
                .stream().map(fulfilled -> new FulfilledReport(fulfilled.getProductId(), fulfilled.getQuantity()))
                .toList();
    }

    public Fulfilled getFulfilled(String id) {
        return fulfillmentRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void addFulfilled(Request request) {
        fulfillmentRepository.save(new Fulfilled(request));
    }

}
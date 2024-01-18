package pl.put.srdsproject.fulfillment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
}

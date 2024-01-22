package pl.put.srdsproject.fulfilled;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.put.srdsproject.request.Request;
import pl.put.srdsproject.util.NotFoundException;

import java.util.LongSummaryStatistics;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingLong;

@Service
@RequiredArgsConstructor
public class FulfilledService {
    private final FulfilledRepository fulfillmentRepository;

    public Map<String, LongSummaryStatistics> getReport() {
        return fulfillmentRepository.getReport()
                .stream().map(fulfilled -> new FulfilledReport(fulfilled.getProductId(), fulfilled.getQuantity()))
                .collect(groupingBy(FulfilledReport::productId, summarizingLong(FulfilledReport::quantity)));
    }

    public Fulfilled getFulfilled(String id) {
        return fulfillmentRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void addFulfilled(Request request) {
        fulfillmentRepository.save(new Fulfilled(request));
    }

}

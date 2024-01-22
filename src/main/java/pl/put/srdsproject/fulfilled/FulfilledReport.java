package pl.put.srdsproject.fulfilled;

import java.util.Map;

public record FulfilledReport(
        int failed,
        int succeeded,
        Map<String, Long> failedProducts,
        Map<String, Long> succeededProducts
) {}
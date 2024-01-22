package pl.put.srdsproject.request;

import java.util.Map;

public record RequestReport(
        int unclaimed,
        int claimed,
        Map<String, Long> products
) {}
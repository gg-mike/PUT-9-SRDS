package pl.put.srdsproject.inventory;

import java.util.Map;

public record InventoryReport(
        Map<String, Long> unclaimed,
        Map<String, Long> claimed,
        Map<String, Long> total
) {}

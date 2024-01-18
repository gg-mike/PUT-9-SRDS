package pl.put.srdsproject.inventory;

public record InventoryReport(
        String productId,
        Long quantity
) {
}

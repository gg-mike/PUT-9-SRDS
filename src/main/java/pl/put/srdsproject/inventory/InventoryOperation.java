package pl.put.srdsproject.inventory;

public record InventoryOperation(
        String productId,
        Long quantity
) {
}

package pl.put.srdsproject.inventory;

public record InventoryOperation(
        InventoryKey inventoryKey,
        Long numberOfItems
) {
}

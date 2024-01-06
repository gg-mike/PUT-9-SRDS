package pl.put.srdsproject.inventory;

public record InventoryOperation(
        InventoryKey sourceInventoryKey,
        InventoryKey targetInventoryKey,
        Long numberOfItems
) {
}

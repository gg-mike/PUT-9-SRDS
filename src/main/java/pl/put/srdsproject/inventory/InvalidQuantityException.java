package pl.put.srdsproject.inventory;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(Inventory inventory, Long numberToRemove) {
        super("Invalid inventory operation: " + inventory + " tried to remove: " + numberToRemove);
    }
}

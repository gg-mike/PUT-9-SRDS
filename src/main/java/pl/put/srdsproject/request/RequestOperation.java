package pl.put.srdsproject.request;

public record RequestOperation(
        String productId,
        Long quantity
) {
}
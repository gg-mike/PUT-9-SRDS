package pl.put.srdsproject.request;

public record RequestOperation(
        String id,
        String productId,
        Long quantity
) {
}
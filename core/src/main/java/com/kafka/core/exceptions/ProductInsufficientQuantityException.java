package com.kafka.core.exceptions;

import java.util.UUID;
import lombok.Getter;

@Getter
public class ProductInsufficientQuantityException extends RuntimeException {
    private final UUID productId;
    private final UUID orderId;

    public ProductInsufficientQuantityException(UUID productId, UUID orderId) {
        super("Product " + productId + " has insufficient quantity in the stock for order " + orderId);
        this.productId = productId;
        this.orderId = orderId;
    }
}

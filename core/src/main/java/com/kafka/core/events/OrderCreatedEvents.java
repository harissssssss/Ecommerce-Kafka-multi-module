package com.kafka.core.events;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderCreatedEvents {
    private UUID orderId;
    private UUID customerId;
    private UUID productId;
    private Integer productQuantity;
}

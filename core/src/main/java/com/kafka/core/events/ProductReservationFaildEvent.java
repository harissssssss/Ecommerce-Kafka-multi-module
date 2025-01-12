package com.kafka.core.events;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReservationFaildEvent {
    private UUID orderId;
    private UUID productId;
    private Integer productQuantity;

}

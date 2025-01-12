package com.kafka.core.events;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentProcessedEvent {
    private UUID orderId;
    private UUID paymentId;
}

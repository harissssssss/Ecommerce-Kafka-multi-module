package com.kafka.core.events;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersApprovedEvent {
    private UUID orderId;
}

package com.kafka.core.dto.commands;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CancelProductReservationCommand {
    private UUID productId;
    private UUID orderId;
    private Integer productQuantity;
}

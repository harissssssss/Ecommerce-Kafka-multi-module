package com.kafka.core.dto.commands;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReserveProductCommand {
    private UUID productId;
    private Integer productQuantity;
    private UUID orderId;
}

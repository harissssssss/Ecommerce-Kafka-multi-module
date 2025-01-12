package com.kafka.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    private UUID id;
    private UUID orderId;
    private UUID productId;
    private BigDecimal productPrice;
    private Integer productQuantity;

}


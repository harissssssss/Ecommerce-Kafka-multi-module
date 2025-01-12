package com.kafka.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private UUID id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}

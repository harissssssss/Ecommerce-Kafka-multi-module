package com.kafka.core.dto;


import com.kafka.core.types.OrderStatus;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private UUID orderId;
    private UUID customerId;
    private UUID productId;
    private Integer productQuantity;
    private OrderStatus status;

}

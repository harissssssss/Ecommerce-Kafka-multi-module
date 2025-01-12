package com.kafka.orderservice.dto;

import com.kafka.core.types.OrderStatus;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryResponse {
    private UUID id;
    private UUID orderId;
    private OrderStatus status;
    private Timestamp createdAt;
}
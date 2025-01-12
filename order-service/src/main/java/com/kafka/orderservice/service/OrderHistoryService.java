package com.kafka.orderservice.service;

import com.kafka.core.types.OrderStatus;
import com.kafka.orderservice.dto.OrderHistory;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryService {
    void add(UUID orderId, OrderStatus orderStatus);

    List<OrderHistory> findByOrderId(UUID orderId);
}

package com.kafka.orderservice.service;

import com.kafka.core.dto.Order;

import java.util.UUID;

public interface OrderService {
    Order placeOrder(Order order);

    void approveOrder(UUID orderId);

    void rejecctOrder(UUID orderId);
}

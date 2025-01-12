package com.kafka.orderservice.service;


import com.kafka.core.dto.Order;
import com.kafka.core.events.OrderCreatedEvents;
import com.kafka.core.types.OrderStatus;
import com.kafka.orderservice.jpa.entity.OrderEntity;
import com.kafka.orderservice.jpa.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String orderEventTopic;

    public OrderServiceImpl(
            OrderRepository orderRepository, KafkaTemplate<String,
            Object> kafkaTemplate, @Value("${orders.events.topic.name}") String orderEventTopic) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.orderEventTopic = orderEventTopic;
    }

    @Override
    public Order placeOrder(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setCustomerId(order.getCustomerId());
        entity.setProductId(order.getProductId());
        entity.setProductQuantity(order.getProductQuantity());
        entity.setStatus(OrderStatus.CREATED);
        orderRepository.save(entity);

        OrderCreatedEvents placedOrder = OrderCreatedEvents.builder()
                .orderId(entity.getId())
                .customerId(entity.getCustomerId())
                .productId(entity.getProductId())
                .productQuantity(entity.getProductQuantity())
                .build();

        kafkaTemplate.send(orderEventTopic, placedOrder);

        return new Order(
                entity.getId(),
                entity.getCustomerId(),
                entity.getProductId(),
                entity.getProductQuantity(),
                entity.getStatus());
    }

    @Override
    public void approveOrder(UUID orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        Assert.notNull(orderEntity, "Order not found with id " + orderId);
        orderEntity.setStatus(OrderStatus.APPROVED);
        orderRepository.save(orderEntity);
    }

    @Override
    public void rejecctOrder(UUID orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        Assert.notNull(orderEntity, "Order not found with id " + orderId);
        orderEntity.setStatus(OrderStatus.REJECTED);
        orderRepository.save(orderEntity);
    }

}

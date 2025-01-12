package com.kafka.orderservice.saga;

import com.kafka.core.dto.commands.*;
import com.kafka.core.events.*;
import com.kafka.core.types.OrderStatus;
import com.kafka.orderservice.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {
        "${orders.events.topic.name}",
        "${products.events.topic.name}",
        "${payments.events.topic.name}"
})
public class OrderSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productCommandTopicName;
    private final String paymentCommandTopicName;
    private final String ordersCommandTopicName;
    private final OrderHistoryService orderHistoryService;

    public OrderSaga(KafkaTemplate<String, Object> kafkaTemplate,
                     @Value("${products.commands.topic.name}") String productCommandTopicName,
                     @Value("${payments.commands.topic.name}") String paymentCommandTopicName,
                     @Value("${orders.commands.topic.name}") String ordersCommandTopicName,
                     OrderHistoryService orderHistoryService) {
        this.kafkaTemplate = kafkaTemplate;
        this.productCommandTopicName = productCommandTopicName;
        this.paymentCommandTopicName = paymentCommandTopicName;
        this.ordersCommandTopicName = ordersCommandTopicName;
        this.orderHistoryService = orderHistoryService;
    }

    @KafkaHandler
    public void handleEvent(@Payload OrderCreatedEvents orderCreatedEvents) {

        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .productId(orderCreatedEvents.getProductId())
                .orderId(orderCreatedEvents.getOrderId())
                .productQuantity(orderCreatedEvents.getProductQuantity())
                .build();

        kafkaTemplate.send(productCommandTopicName, reserveProductCommand);

        orderHistoryService.add(orderCreatedEvents.getOrderId(), OrderStatus.CREATED);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservedEvent productReservedEvent) {

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .productId(productReservedEvent.getProductId())
                .productQuantity(productReservedEvent.getProductQuantity())
                .productPrice(productReservedEvent.getProductPrice())
                .build();

        kafkaTemplate.send(paymentCommandTopicName, processPaymentCommand);

    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentProcessedEvent event) {
        ApproveOrderCommand approveOrderCommand = ApproveOrderCommand.builder()
                .orderId(event.getOrderId())
                .build();
        kafkaTemplate.send(ordersCommandTopicName, approveOrderCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload OrdersApprovedEvent ordersApprovedEvent) {
        orderHistoryService.add(ordersApprovedEvent.getOrderId(), OrderStatus.APPROVED);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentFailedEvent event) {
        CancelProductReservationCommand cancelProductReservationCommand=
                CancelProductReservationCommand.builder()
                        .orderId(event.getOrderId())
                        .productId(event.getProductId())
                        .productQuantity(event.getProductQuantity())
                        .build();
        kafkaTemplate.send(productCommandTopicName, cancelProductReservationCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservationCancelledEvent productReservationCancelledEvent) {
        RejectOrderCommand rejectOrderCommand = RejectOrderCommand.builder()
                .orderId(productReservationCancelledEvent.getOrderId())
                .build();
        kafkaTemplate.send(ordersCommandTopicName, rejectOrderCommand);
        orderHistoryService.add(productReservationCancelledEvent.getOrderId(), OrderStatus.REJECTED);
    }


}

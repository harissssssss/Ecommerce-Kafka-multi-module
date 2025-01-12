package com.kafka.orderservice.service.handler;

import com.kafka.core.dto.commands.ApproveOrderCommand;
import com.kafka.core.dto.commands.RejectOrderCommand;
import com.kafka.core.events.OrdersApprovedEvent;
import com.kafka.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${orders.commands.topic.name}")
public class OrdersCommandsHandler {

    private final OrderService orderService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String OrdersEventTopicName;

    public OrdersCommandsHandler(OrderService orderService, KafkaTemplate<String, Object> kafkaTemplate,
                                 @Value("${orders.events.topic.name}") String ordersEventTopicName) {
        this.orderService = orderService;
        this.kafkaTemplate = kafkaTemplate;
        OrdersEventTopicName = ordersEventTopicName;
    }

    @KafkaHandler
    public void handler(@Payload ApproveOrderCommand command) {
        orderService.approveOrder(command.getOrderId());

        OrdersApprovedEvent ordersApprovedEvent = OrdersApprovedEvent.builder()
                .orderId(command.getOrderId())
                .build();

        kafkaTemplate.send(OrdersEventTopicName, ordersApprovedEvent);

    }

    @KafkaHandler
    public void handler(@Payload RejectOrderCommand rejectOrderCommand) {
        orderService.rejecctOrder(rejectOrderCommand.getOrderId());
    }
}

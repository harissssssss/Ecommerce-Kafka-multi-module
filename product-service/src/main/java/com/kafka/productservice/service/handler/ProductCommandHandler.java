package com.kafka.productservice.service.handler;

import com.kafka.core.dto.Product;
import com.kafka.core.dto.commands.CancelProductReservationCommand;
import com.kafka.core.dto.commands.ReserveProductCommand;
import com.kafka.core.events.ProductReservationCancelledEvent;
import com.kafka.core.events.ProductReservationFaildEvent;
import com.kafka.core.events.ProductReservedEvent;
import com.kafka.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@KafkaListener(topics = "${products.commands.topic.name}")
public class ProductCommandHandler {

    private final ProductService productService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productEventsTopicName;

    public ProductCommandHandler(ProductService productService, KafkaTemplate<String, Object> kafkaTemplate, @Value("${products.events.topic.name}") String productEventsTopicName) {
        this.productService = productService;
        this.kafkaTemplate = kafkaTemplate;
        this.productEventsTopicName = productEventsTopicName;
    }

    @KafkaHandler
    public void handleCommand(@Payload ReserveProductCommand reserveProductCommand) {
        try {
            Product product = Product.builder()
                    .id(reserveProductCommand.getProductId())
                    .quantity(reserveProductCommand.getProductQuantity())
                    .build();
            var reservedProduct = productService.reserve(product, reserveProductCommand.getOrderId());
            ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                    .orderId(reserveProductCommand.getOrderId())
                    .productId(reserveProductCommand.getProductId())
                    .productQuantity(reserveProductCommand.getProductQuantity())
                    .productPrice(reservedProduct.getPrice())
                    .build();
            kafkaTemplate.send(productEventsTopicName, productReservedEvent);
        } catch (Exception e) {

            log.error(e.getMessage());
            ProductReservationFaildEvent productReservationFaildEvent = ProductReservationFaildEvent.builder()
                    .orderId(reserveProductCommand.getOrderId())
                    .productId(reserveProductCommand.getProductId())
                    .productQuantity(reserveProductCommand.getProductQuantity())
                    .build();
            kafkaTemplate.send(productEventsTopicName, productReservationFaildEvent);
        }

    }

    @KafkaHandler
    public void handleCommand(@Payload CancelProductReservationCommand cancelProductReservationCommand) {
        Product productToCancel = Product.builder()
                .id(cancelProductReservationCommand.getProductId())
                .quantity(cancelProductReservationCommand.getProductQuantity())
                .build();
        productService.cancelReservation(productToCancel, cancelProductReservationCommand.getOrderId());

        ProductReservationCancelledEvent productReservationCancelledEvent = ProductReservationCancelledEvent.builder()
                .orderId(cancelProductReservationCommand.getOrderId())
                .productId(cancelProductReservationCommand.getProductId())
                .build();
        kafkaTemplate.send(productEventsTopicName, productReservationCancelledEvent);
    }

}

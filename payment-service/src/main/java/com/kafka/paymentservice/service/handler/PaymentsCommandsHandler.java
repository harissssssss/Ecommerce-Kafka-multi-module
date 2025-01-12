package com.kafka.paymentservice.service.handler;

import com.kafka.core.dto.Payment;
import com.kafka.core.events.PaymentFailedEvent;
import com.kafka.core.events.PaymentProcessedEvent;
import com.kafka.core.dto.commands.ProcessPaymentCommand;
import com.kafka.core.exceptions.CreditCardProcessorUnavailableException;
import com.kafka.paymentservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@KafkaListener(topics = "${payments.commands.topic.name}")
public class PaymentsCommandsHandler {

    private final PaymentService paymentService;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String paymentsEventsTopicName;

    public PaymentsCommandsHandler(PaymentService paymentService, KafkaTemplate<String, Object> kafkaTemplate,
                                   @Value("${payments.events.topic.name}") String paymentsEventsTopicName) {
        this.paymentService = paymentService;
        this.kafkaTemplate = kafkaTemplate;
        this.paymentsEventsTopicName = paymentsEventsTopicName;
    }

    @KafkaHandler
    public void handler(@Payload ProcessPaymentCommand command) {
        try {
            Payment payment = Payment.builder()
                    .orderId(command.getOrderId())
                    .productId(command.getProductId())
                    .productPrice(command.getProductPrice())
                    .productQuantity(command.getProductQuantity())
                    .build();
            paymentService.process(payment);

            PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent.builder()
                    .orderId(command.getOrderId())
                    .paymentId(payment.getId())
                    .build();
            kafkaTemplate.send(paymentsEventsTopicName, paymentProcessedEvent);


        } catch (CreditCardProcessorUnavailableException e) {
            log.error("CreditCardProcessorUnavailableException", e.getMessage());

            PaymentFailedEvent paymentFailedEvent = PaymentFailedEvent.builder()
                    .orderId(command.getOrderId())
                    .productId(command.getProductId())
                    .productQuantity(command.getProductQuantity())
                    .build();

            kafkaTemplate.send(paymentsEventsTopicName, paymentFailedEvent);
        }

    }
}

package com.kafka.paymentservice.service;

import com.kafka.core.dto.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment process(Payment payment);
}

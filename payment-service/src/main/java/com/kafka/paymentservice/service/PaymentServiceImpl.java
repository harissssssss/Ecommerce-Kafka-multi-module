package com.kafka.paymentservice.service;

import com.kafka.core.dto.Payment;
import com.kafka.paymentservice.jpa.entity.PaymentEntity;
import com.kafka.paymentservice.jpa.repository.PaymentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    public static final String SAMPLE_CREDIT_CARD_NUMBER = "374245455400126";
    private final PaymentRepository paymentRepository;
    private final CreditCardProcessorRemoteService creditCardProcessorRemoteService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, CreditCardProcessorRemoteService creditCardProcessorRemoteService) {
        this.paymentRepository = paymentRepository;
        this.creditCardProcessorRemoteService = creditCardProcessorRemoteService;
    }

    @Override
    public Payment process(Payment payment) {
        BigDecimal totalPrice = payment.getProductPrice()
                .multiply(new BigDecimal(payment.getProductQuantity()));
        creditCardProcessorRemoteService.process(new BigInteger(SAMPLE_CREDIT_CARD_NUMBER), totalPrice);
        PaymentEntity paymentEntity = new PaymentEntity();
        BeanUtils.copyProperties(payment, paymentEntity);
        paymentRepository.save(paymentEntity);

        var processedPayment = new Payment();
        BeanUtils.copyProperties(payment, processedPayment);
        processedPayment.setId(paymentEntity.getId());
        return processedPayment;
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll().stream().map(entity -> new Payment(entity.getId(), entity.getOrderId(), entity.getProductId(), entity.getProductPrice(), entity.getProductQuantity())
        ).collect(Collectors.toList());
    }
}

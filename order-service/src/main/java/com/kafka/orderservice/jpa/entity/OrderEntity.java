package com.kafka.orderservice.jpa.entity;

import com.kafka.core.types.OrderStatus;
import jakarta.persistence.*;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "status")
    private OrderStatus status;
    @Column(name = "customer_id")
    private UUID customerId;
    @Column(name = "product_id")
    private UUID productId;
    @Column(name = "product_quantity")
    private Integer productQuantity;
}
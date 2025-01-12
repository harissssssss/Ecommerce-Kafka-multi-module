package com.kafka.orderservice.jpa.entity;

import com.kafka.core.types.OrderStatus;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders_history")
@Entity
public class OrderHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "status")
    private OrderStatus status;
    @Column(name = "created_at")
    private Timestamp createdAt;
}
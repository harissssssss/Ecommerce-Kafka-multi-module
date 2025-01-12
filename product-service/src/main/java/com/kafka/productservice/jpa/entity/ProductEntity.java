package com.kafka.productservice.jpa.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private BigDecimal price;
}

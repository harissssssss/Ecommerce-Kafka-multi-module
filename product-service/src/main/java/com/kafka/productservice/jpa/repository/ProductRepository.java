package com.kafka.productservice.jpa.repository;

import com.kafka.productservice.jpa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}

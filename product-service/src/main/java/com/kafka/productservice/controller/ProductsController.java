package com.kafka.productservice.controller;

import com.kafka.core.dto.Product;
import com.kafka.productservice.dto.ProductCreationRequest;
import com.kafka.productservice.dto.ProductCreationResponse;
import com.kafka.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> findAll() {
        return productService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCreationResponse save(@RequestBody @Valid ProductCreationRequest request) {
        var product = new Product();
        BeanUtils.copyProperties(request, product);
        Product result = productService.save(product);

        var productCreationResponse = new ProductCreationResponse();
        BeanUtils.copyProperties(result, productCreationResponse);
        return productCreationResponse;
    }
}

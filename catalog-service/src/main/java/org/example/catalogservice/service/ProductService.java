package org.example.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.example.catalogservice.entity.Product;
import org.example.catalogservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Optional<Product> findById(String id) {
        return repository.findById(id);
    }

    // Если потом добавишь создание товара для Kafka
    public Product save(Product product) {
        return repository.save(product);
    }
}
package org.example.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.example.catalogservice.entity.Product;
import org.example.catalogservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    @Transactional(readOnly = true)
    public Optional<Product> findById(String id) {
        return repository.findById(id);
    }
    public List<Product> findAll(){
        return repository.findAll();
    }

    // Если потом добавишь создание товара для Kafka
    public Product save(Product product) {
        return repository.save(product);
    }
}
package org.example.inventoryservice.service;
import lombok.RequiredArgsConstructor;
import org.example.inventoryservice.entity.ItemStock;
import org.example.inventoryservice.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository repository;

    public Optional<ItemStock> findByProductId(String productId) {
        return repository.findById(productId);
    }
}
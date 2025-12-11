package org.example.inventoryservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.inventoryservice.entity.ItemStock;
import org.example.inventoryservice.service.StockService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@Profile("rest")
@AllArgsConstructor
@Slf4j
public class StockRestController {
    StockService stockService;

    @GetMapping("/{productId}")
    public ResponseEntity<ItemStock> getStock(@PathVariable String productId) {
        log.info("REST request received for ProductID: {}", productId);

        return stockService.findByProductId(productId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Stock not found for ProductID: {}", productId);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ItemStock>> getStockBatch(@RequestBody List<String> productIds) {
        log.info("REST batch request for {} items", productIds.size());

        // Используем стандартный метод JPA
        List<ItemStock> stocks = stockService.findAllById(productIds);

        return ResponseEntity.ok(stocks);
    }

}

package org.example.inventoryservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.inventoryservice.entity.ItemStock;
import org.example.inventoryservice.service.StockService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

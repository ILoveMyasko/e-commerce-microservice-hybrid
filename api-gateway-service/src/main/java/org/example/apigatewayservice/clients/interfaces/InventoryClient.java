package org.example.apigatewayservice.clients.interfaces;

public interface InventoryClient {
    Integer getStock(String productId);
}

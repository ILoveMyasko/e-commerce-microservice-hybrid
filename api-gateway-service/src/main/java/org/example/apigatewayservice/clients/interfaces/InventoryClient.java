package org.example.apigatewayservice.clients.interfaces;

import java.util.List;
import java.util.Map;

public interface InventoryClient {
    Integer getStock(String productId);
    Map<String, Integer> getStockBatch(List<String> productIds);
}

package org.example.apigatewayservice.clients.interfaces;

import org.example.apigatewayservice.dto.ProductAggregateDto;

public interface CatalogClient {
    ProductAggregateDto getProductBase(String id);
}

package org.example.apigatewayservice.clients.interfaces;

import org.example.apigatewayservice.dto.ProductAggregateDto;

import java.util.List;

public interface CatalogClient {
    ProductAggregateDto getProductBase(String id);
    List<ProductAggregateDto> getAllProducts();
}

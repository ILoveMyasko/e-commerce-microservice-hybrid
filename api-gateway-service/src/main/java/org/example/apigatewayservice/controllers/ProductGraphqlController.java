package org.example.apigatewayservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.apigatewayservice.clients.interfaces.CatalogClient;
import org.example.apigatewayservice.clients.interfaces.InventoryClient;
import org.example.apigatewayservice.dto.ProductAggregateDto;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProductGraphqlController {

    private final CatalogClient catalogClient;
    private final InventoryClient inventoryClient;
    //TODO: Completable future for paralleling
    @QueryMapping
    public ProductAggregateDto product(@Argument String id) {
        // 1. Получаем данные о товаре (Name, Price)
        ProductAggregateDto product = catalogClient.getProductBase(id);

        // 2. Получаем остатки
        Integer stock = inventoryClient.getStock(id);

        // 3. Обогащаем (Enrich) объект
        product.setQuantity(stock);

        return product;
    }
}
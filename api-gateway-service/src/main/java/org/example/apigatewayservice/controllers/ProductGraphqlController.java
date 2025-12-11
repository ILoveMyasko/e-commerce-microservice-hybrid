package org.example.apigatewayservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.apigatewayservice.clients.interfaces.CatalogClient;
import org.example.apigatewayservice.clients.interfaces.InventoryClient;
import org.example.apigatewayservice.dto.ProductAggregateDto;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

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

    @QueryMapping
    public List<ProductAggregateDto> products() {
        List<ProductAggregateDto> products = catalogClient.getAllProducts();
        if (products.isEmpty()) {
            return products;
        }

        // 2. Собираем все ID товаров в список
        List<String> productIds = products.stream()
                .map(ProductAggregateDto::getId)
                .toList();

        // 3. Делаем BATCH запрос к складу (1 вызов вместо 50!)
        Map<String, Integer> stockMap = inventoryClient.getStockBatch(productIds);

        // 4. Обогащаем товары данными из Map
        products.forEach(product -> {
            // Берем из мапы, если нет - ставим 0
            Integer quantity = stockMap.getOrDefault(product.getId(), 0);
            product.setQuantity(quantity);
        });

        return products;
    }
}
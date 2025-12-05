package org.example.apigatewayservice.clients;

import lombok.RequiredArgsConstructor;
import org.example.apigatewayservice.clients.interfaces.CatalogClient;
import org.example.apigatewayservice.dto.ProductAggregateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("rest")
@RequiredArgsConstructor
public class RestCatalogClient implements CatalogClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.catalog.url}")
    private String url;

    @Override
    public ProductAggregateDto getProductBase(String id) {
        // Делаем GET запрос, Spring сам мапит JSON в наш DTO (по полям)
        return restTemplate.getForObject(url + "/api/products/" + id, ProductAggregateDto.class);
    }
}
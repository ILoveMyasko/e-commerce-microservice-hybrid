package org.example.apigatewayservice.clients;

import lombok.RequiredArgsConstructor;
import org.example.apigatewayservice.clients.interfaces.InventoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("rest")
@RequiredArgsConstructor
public class RestInventoryClient implements InventoryClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.inventory.url}")
    private String url;

    @Override
    public Integer getStock(String productId) {
        return restTemplate.getForObject(url + "api/stock/" + productId, Integer.class);
    }
}

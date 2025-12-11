package org.example.apigatewayservice.clients;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.apigatewayservice.clients.interfaces.InventoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Profile({"rest",  "grpc_catalog"})
@RequiredArgsConstructor
public class RestInventoryClient implements InventoryClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.inventory.url}")
    private String url;

    @Override
    public Integer getStock(String productId) {
       // return restTemplate.getForObject(url + "/api/stock/" + productId, Integer.class);
        StockDto response = restTemplate.getForObject(
                url + "/api/stock/" + productId,
                StockDto.class
        );
        return response != null ? response.getQuantity() : 0;
    }

    @Override
    public Map<String, Integer> getStockBatch(List<String> productIds) {
        // 1. Упаковываем список ID в HttpEntity (тело запроса)
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(productIds);

        // 2. Делаем POST запрос
        var response = restTemplate.exchange(
                url + "/api/stock/batch",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<StockDto>>() {}
        );

        List<StockDto> stockList = response.getBody();

        if (stockList == null) return Map.of();

        // 3. Превращаем Список в Map (как и в gRPC)
        return stockList.stream()
                .collect(Collectors.toMap(
                        StockDto::getProductId,
                        StockDto::getQuantity
                ));
    }

    // Внутренний класс для маппинга JSON ответа
    @Data
    private static class StockDto {
        private String productId;
        private Integer quantity;
    }
}

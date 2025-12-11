package org.example.apigatewayservice.clients;

import com.example.ecommerce.grpc.inventory.InventoryServiceGrpc;
import com.example.ecommerce.grpc.inventory.StockBatchRequest;
import com.example.ecommerce.grpc.inventory.StockRequest;
import com.example.ecommerce.grpc.inventory.StockResponse;
import lombok.RequiredArgsConstructor;
import org.example.apigatewayservice.clients.interfaces.InventoryClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Profile({"grpc","grpc_inventory"})
@RequiredArgsConstructor
public class GrpcInventoryClient implements InventoryClient {

    private final InventoryServiceGrpc.InventoryServiceBlockingStub stub;

    @Override
    public Integer getStock(String productId) {
        var response = stub.getStock(
                StockRequest.newBuilder().setProductId(productId).build()
        );
        return response.getQuantity();
    }

    @Override
    public Map<String, Integer> getStockBatch(List<String> productIds) {
        // 1. Формируем gRPC запрос
        StockBatchRequest request = StockBatchRequest.newBuilder()
                .addAllProductIds(productIds)
                .build();

        // 2. Делаем вызов
        var response = stub.getStockBatch(request);

        // 3. Превращаем список ответов в Map<String, Integer>
        return response.getStocksList().stream()
                .collect(Collectors.toMap(
                        StockResponse::getProductId, // Ключ
                        StockResponse::getQuantity   // Значение
                ));
    }
}
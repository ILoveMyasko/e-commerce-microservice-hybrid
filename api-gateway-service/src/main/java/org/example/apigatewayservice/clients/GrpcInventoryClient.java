package org.example.apigatewayservice.clients;

import com.example.ecommerce.grpc.inventory.InventoryServiceGrpc;
import com.example.ecommerce.grpc.inventory.StockRequest;
import lombok.RequiredArgsConstructor;
import org.example.apigatewayservice.clients.interfaces.InventoryClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("grpc")
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
}
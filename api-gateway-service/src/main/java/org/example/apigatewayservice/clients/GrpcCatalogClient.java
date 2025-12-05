package org.example.apigatewayservice.clients;


// Импорты из common-proto
import com.example.ecommerce.grpc.catalog.CatalogServiceGrpc;
import com.example.ecommerce.grpc.catalog.ProductRequest;
import com.example.ecommerce.grpc.catalog.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.example.apigatewayservice.clients.interfaces.CatalogClient;
import org.example.apigatewayservice.dto.ProductAggregateDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("grpc")
@RequiredArgsConstructor
public class GrpcCatalogClient implements CatalogClient {
    // Имя "catalog-service" должно совпадать с конфигом в properties
    private final CatalogServiceGrpc.CatalogServiceBlockingStub stub;

    @Override
    public ProductAggregateDto getProductBase(String id) {
        ProductResponse response = stub.getProduct(
                ProductRequest.newBuilder().setId(id).build()
        );

        return ProductAggregateDto.builder()
                .id(response.getId())
                .name(response.getName())
                .description(response.getDescription())
                .price(response.getPrice())
                .build();
    }
}
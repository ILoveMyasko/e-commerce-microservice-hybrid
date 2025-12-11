package org.example.apigatewayservice.clients;


// Импорты из common-proto
import com.example.ecommerce.grpc.catalog.CatalogServiceGrpc;
import com.example.ecommerce.grpc.catalog.ListProductResponse;
import com.example.ecommerce.grpc.catalog.ProductRequest;
import com.example.ecommerce.grpc.catalog.ProductResponse;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.example.apigatewayservice.clients.interfaces.CatalogClient;
import org.example.apigatewayservice.dto.ProductAggregateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Profile({"grpc","grpc_catalog"})
public class GrpcCatalogClient implements CatalogClient {
    // Имя "catalog-service" должно совпадать с конфигом в properties
    private static final int CHANNEL_COUNT = 20;
    private final List<CatalogServiceGrpc.CatalogServiceBlockingStub> stubs = new ArrayList<>();
    private final List<ManagedChannel> channels = new ArrayList<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    public GrpcCatalogClient(
            @Value("${grpc.catalog.host:127.0.0.1}") String host,
            @Value("${grpc.catalog.port:8081}") int port
    ) {
        for (int i = 0; i < CHANNEL_COUNT; i++) {
            // Создаем канал
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .maxInboundMessageSize(20 * 1024 * 1024)
                    .keepAliveTime(30, TimeUnit.SECONDS)
                    .keepAliveTimeout(10, TimeUnit.SECONDS)
                    .keepAliveWithoutCalls(true)
                    .build();

            channels.add(channel);
            // Создаем стаб для этого канала и сохраняем
            stubs.add(CatalogServiceGrpc.newBlockingStub(channel));
        }
    }

    private CatalogServiceGrpc.CatalogServiceBlockingStub getStub() {
        int index = Math.abs(counter.getAndIncrement() % stubs.size());
        return stubs.get(index);
    }

    @Override
    public ProductAggregateDto getProductBase(String id) {
        ProductResponse response = getStub().getProduct(
                ProductRequest.newBuilder().setId(id).build()
        );

        return ProductAggregateDto.builder()
                .id(response.getId())
                .name(response.getName())
                .description(response.getDescription())
                .price(response.getPrice())
                .priceHistory(response.getPriceHistoryList())
                .build();
    }
    @Override
    public List<ProductAggregateDto> getAllProducts() {
        // Используем стаб из пула
        var response = getStub().getAllProducts(Empty.getDefaultInstance());

        return response.getProductsList().stream()
                .map(this::mapToDto)
                .toList();
    }

    private ProductAggregateDto mapToDto(ProductResponse proto) {
        return ProductAggregateDto.builder()
                .id(proto.getId())
                .name(proto.getName())
                .description(proto.getDescription()) // Длинное описание
                .price(proto.getPrice())
                .priceHistory(proto.getPriceHistoryList())
                .build();
    }

    // Обязательно закрываем каналы при остановке приложения
    @PreDestroy
    public void shutdown() {
        for (ManagedChannel channel : channels) {
            try {
                channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
//    @Override
//    public List<ProductAggregateDto> getAllProducts() {
//        ListProductResponse response = getStub().getAllProducts(Empty.getDefaultInstance());
//        System.out.println(response.getProductsList().stream().toList());
//        return response.getProductsList().stream()
//                .map(proto->ProductAggregateDto.builder()
//                        .id(proto.getId())
//                        .name(proto.getName())
//                        .price(proto.getPrice())
//                        .description(proto.getDescription())
//                        .build())
//                .toList();
//
//    }
}
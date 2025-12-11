package org.example.apigatewayservice.configs;

import com.example.ecommerce.grpc.catalog.CatalogServiceGrpc;
import com.example.ecommerce.grpc.inventory.InventoryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.catalog.host:localhost}")
    private String catalogHost;

    @Value("${grpc.catalog.port:9091}")
    private int catalogPort;

    @Value("${grpc.inventory.host:localhost}")
    private String inventoryHost;

    @Value("${grpc.inventory.port:9092}")
    private int inventoryPort;
    // 1. Создаем бин для Inventory Client
    @Bean
    public InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(inventoryHost, inventoryPort)
                .usePlaintext()
                .build();

        return InventoryServiceGrpc.newBlockingStub(channel);
    }

    // 2. Создаем бин для Catalog Client
//    @Bean
//    public CatalogServiceGrpc.CatalogServiceBlockingStub catalogStub() {
//        // Создаем канал вручную
//        ManagedChannel channel = ManagedChannelBuilder.forAddress(catalogHost, catalogPort)
//                .usePlaintext() // ВАЖНО: Отключаем SSL (аналог negotiation-type=plaintext)
//                .maxInboundMessageSize(20 * 1024 * 1024)
//                .build();
//
//        return CatalogServiceGrpc.newBlockingStub(channel)
//                .withCompression("gzip");
//    }

}
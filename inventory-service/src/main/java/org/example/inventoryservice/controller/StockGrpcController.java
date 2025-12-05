package org.example.inventoryservice.controller;

import com.example.ecommerce.grpc.inventory.InventoryServiceGrpc;
import com.example.ecommerce.grpc.inventory.StockRequest;
import com.example.ecommerce.grpc.inventory.StockResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.inventoryservice.service.StockService;
import org.springframework.context.annotation.Profile;
import org.springframework.grpc.server.service.GrpcService;
@Slf4j
@GrpcService
@Profile("grpc")
@RequiredArgsConstructor
public class StockGrpcController extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final StockService stockService;

    @Override
    public void getStock(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        String productId = request.getProductId();
        log.info("gRPC request received for ProductID: {}", productId);

        stockService.findByProductId(productId).ifPresentOrElse(
                stock -> {
                    StockResponse response = StockResponse.newBuilder()
                            .setProductId(stock.getProductId())
                            .setQuantity(stock.getQuantity())
                            .build();

                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                },
                () -> {
                    log.warn("Stock not found for ProductID: {}", productId);
                    // Отдаем 0, если нет записи, чтобы не ломать агрегацию
                    StockResponse response = StockResponse.newBuilder().setProductId(productId).setQuantity(0).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
        );
    }

}

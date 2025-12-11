package org.example.inventoryservice.controller;

import com.example.ecommerce.grpc.inventory.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.inventoryservice.entity.ItemStock;
import org.example.inventoryservice.service.StockService;
import org.springframework.context.annotation.Profile;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;

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

    @Override
    public void getStockBatch(StockBatchRequest request, StreamObserver<StockBatchResponse> responseObserver) {
        // 1. Достаем список ID из запроса
         List<String> requestedIds = request.getProductIdsList();

        // 2. Идем в базу одним запросом (SELECT ... WHERE id IN (...))
        List<ItemStock> stockEntities = stockService.findAllById(requestedIds);

        // 3. Превращаем Entity в Proto
        List<StockResponse> protoStocks = stockEntities.stream()
                .map(stock -> StockResponse.newBuilder()
                        .setProductId(stock.getProductId())
                        .setQuantity(stock.getQuantity())
                        .build())
                .toList();

        // 4. Отправляем ответ
        StockBatchResponse response = StockBatchResponse.newBuilder()
                .addAllStocks(protoStocks)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}

package org.example.catalogservice.controller;

// Импорты из нашего common-proto
import com.example.ecommerce.grpc.catalog.CatalogServiceGrpc;
import com.example.ecommerce.grpc.catalog.ProductRequest;
import com.example.ecommerce.grpc.catalog.ProductResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.catalogservice.service.ProductService;
import org.springframework.context.annotation.Profile;

@Slf4j
@GrpcService
@Profile("grpc")
@RequiredArgsConstructor
public class ProductGrpcController extends CatalogServiceGrpc.CatalogServiceImplBase {

    // Внедряем ту же самую бизнес-логику, что и в REST-контроллере
    private final ProductService productService;

    @Override
    public void getProduct(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        String id = request.getId();
        log.info("gRPC call: getProduct for ID: {}", id);

        productService.findById(id).ifPresentOrElse(
                // Если товар найден:
                product -> {
                    // Маппинг (Entity -> Proto)
                    ProductResponse response = ProductResponse.newBuilder()
                            .setId(product.getId())
                            .setName(product.getName())
                            .setDescription(product.getDescription())
                            .setPrice(product.getPrice())
                            .build();

                    // Отправка ответа
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                },
                // Если товар не найден:
                () -> {
                    responseObserver.onError(
                            io.grpc.Status.NOT_FOUND
                                    .withDescription("Product not found with ID: " + id)
                                    .asRuntimeException()
                    );
                }
        );
    }
}
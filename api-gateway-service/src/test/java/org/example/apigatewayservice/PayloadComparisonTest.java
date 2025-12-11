package org.example.apigatewayservice;

import com.example.ecommerce.grpc.catalog.ListProductResponse;
import com.example.ecommerce.grpc.catalog.ProductResponse;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PayloadComparisonTest {

    private static final int ITEM_COUNT = 1000;
    private static final int HISTORY_SIZE = 100; // 100 записей истории для каждого товара

    // DTO для JSON
    static class ProductJsonDto {
        public String id;
        public String name;
        public String description;
        public Double price;
        public List<Integer> priceHistory; // Список чисел

        public ProductJsonDto(String id, String name, String description, Double price, List<Integer> priceHistory) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.priceHistory = priceHistory;
        }
    }

    @Test
    void comparePayloadSizes() throws IOException {
        // 1. Генерация данных
        // Делаем описание коротким, чтобы увидеть эффект сжатия чисел
        //String shortDescription = "Short description.";

        List<ProductResponse> protoList = new ArrayList<>();
        List<ProductJsonDto> jsonList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < ITEM_COUNT; i++) {
            String id = String.valueOf(i);
            String name = "" + i;
            double price = 1.0 + i;
            String longDescription = "abcde ";
            List<Integer> history = new ArrayList<>();
            for (int j = 0; j < HISTORY_SIZE; j++) {
                history.add(1000 + random.nextInt(5000));
            }

            // Protobuf
            protoList.add(ProductResponse.newBuilder()
                    .setId(id)
                    .setName(name)
                    .setDescription(longDescription)
                    .setPrice(price)
                    .setDescription(longDescription)
                    .addAllPriceHistory(history) // Добавляем массив
                    .build());

            // JSON
            jsonList.add(new ProductJsonDto(id, name, longDescription, price, history));
        }

        // 2. Сериализация Proto
        ListProductResponse protoWrapper = ListProductResponse.newBuilder()
                .addAllProducts(protoList)
                .build();
        byte[] protoBytes = protoWrapper.toByteArray();

        // 3. Сериализация JSON
        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonBytes = mapper.writeValueAsBytes(jsonList);

        // 4. Вывод
        double jsonSizeKb = jsonBytes.length / 1024.0;
        double protoSizeKb = protoBytes.length / 1024.0;
        double ratio = jsonSizeKb / protoSizeKb;
        double savings = (1.0 - (protoSizeKb / jsonSizeKb)) * 100.0;

        System.out.println("==================================================");
        System.out.println("       SCIENTIFIC PAYLOAD COMPARISON (NUMBERS)    ");
        System.out.println("==================================================");
        System.out.printf("Items: %d, History Array Size: %d ints per item%n", ITEM_COUNT, HISTORY_SIZE);
        System.out.println("--------------------------------------------------");
        System.out.printf("JSON Size:  %.2f KB%n", jsonSizeKb);
        System.out.printf("Proto Size: %.2f KB%n", protoSizeKb);
        System.out.println("--------------------------------------------------");
        System.out.printf("Difference: %.2f KB saved%n", jsonSizeKb - protoSizeKb);
        System.out.printf("Efficiency: gRPC is %.2fx smaller%n", ratio);
        System.out.printf("Traffic Savings: %.2f%%%n", savings);
        System.out.println("==================================================");
    }
}
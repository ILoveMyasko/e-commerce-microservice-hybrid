package org.example.inventoryservice;

import org.example.inventoryservice.entity.ItemStock;
import org.example.inventoryservice.repository.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StockRepository repo) {
        return args -> {
            List<ItemStock> products = new ArrayList<>();
            for (int i = 1; i <= 1000; i++) {
                products.add(new ItemStock(
                        String.valueOf(i),
                        i
                ));
            }
            repo.saveAll(products);
            System.out.println("--- GENERATED 1000 PRODUCTS ---");
        };
    }
}

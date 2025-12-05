package org.example.inventoryservice;

import org.example.inventoryservice.entity.ItemStock;
import org.example.inventoryservice.repository.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StockRepository repo) {
        return args -> {
            repo.save(new ItemStock("1", 500));
            repo.save(new ItemStock("2", 150));
            repo.save(new ItemStock("3", 0));
            System.out.println("--- INVENTORY TEST DATA LOADED ---");
        };
    }
}

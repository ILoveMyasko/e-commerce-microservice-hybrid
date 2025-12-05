package org.example.catalogservice;

import org.example.catalogservice.entity.Product;
import org.example.catalogservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ProductRepository repo) {
        return args -> {
            repo.save(new Product("1", "iPhone 15", "Cool phone", 999.99));
            repo.save(new Product("2", "MacBook Pro", "Cool laptop", 1999.99));
            System.out.println("--- TEST DATA LOADED ---");
        };
    }
}

package org.example.catalogservice;

import org.example.catalogservice.entity.Product;
import org.example.catalogservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class CatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ProductRepository repo) {
        List<Integer> history = new ArrayList<>();
        Random random = new Random();
        for (int j = 0; j < 500; j++) {
            history.add(1000 + random.nextInt(5000));
        }
        String heavyDescription = "abcde";
        return args -> {
            List<Product> products = new ArrayList<>();
            for (int i = 1; i <= 1000; i++) {
                products.add(new Product(
                        String.valueOf(i),
                        "Product " + i,
                        heavyDescription,
                        10999.0 + i,
                        history
                ));
            }
            repo.saveAll(products);
            System.out.println("--- GENERATED 1000 PRODUCTS ---");
        };
    }
}

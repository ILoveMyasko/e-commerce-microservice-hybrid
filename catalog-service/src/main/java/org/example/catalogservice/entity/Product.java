package org.example.catalogservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private String id;
    private String name;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private Double price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_price_history", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "price")
    private List<Integer> priceHistory;
}

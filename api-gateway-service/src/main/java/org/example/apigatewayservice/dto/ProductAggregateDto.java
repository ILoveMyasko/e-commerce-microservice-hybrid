package org.example.apigatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAggregateDto {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private List<Integer> priceHistory;
}
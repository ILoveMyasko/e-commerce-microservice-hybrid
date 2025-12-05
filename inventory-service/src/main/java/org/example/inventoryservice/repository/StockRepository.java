package org.example.inventoryservice.repository;

import org.example.inventoryservice.entity.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<ItemStock,String> {
}

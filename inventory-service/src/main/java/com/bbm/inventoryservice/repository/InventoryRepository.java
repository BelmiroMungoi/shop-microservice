package com.bbm.inventoryservice.repository;

import java.util.List;

import com.bbm.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	List<Inventory> findBySkuCodeIn(List<String> skuCode);
}

package com.bbm.inventoryservice.repository;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.introspect.AnnotationCollector;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bbm.inventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	List<Inventory> findBySkuCodeIn(List<String> skuCode);
}

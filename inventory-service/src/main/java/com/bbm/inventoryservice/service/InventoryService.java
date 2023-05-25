package com.bbm.inventoryservice.service;

import com.bbm.inventoryservice.model.Inventory;
import com.bbm.inventoryservice.model.dto.InventoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbm.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

	private final InventoryRepository inventoryRepository;
	
	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCode) {
		return inventoryRepository.findBySkuCodeIn(skuCode).stream()
				.map(inventory ->
						InventoryResponse.builder()
								.skuCode(inventory.getSkuCode())
								.isInStock(inventory.getQuantity() > 0)
								.build()
				).toList();
	}
}

package com.bbm.inventoryservice.service;

import com.bbm.inventoryservice.model.dto.InventoryResponse;
import com.bbm.inventoryservice.repository.InventoryRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

	private final InventoryRepository inventoryRepository;
	
	@Transactional(readOnly = true)
	@SneakyThrows
	public List<InventoryResponse> isInStock(List<String> skuCode) {
		log.info("Wait Started");
		Thread.sleep(10000);
		log.info("Wait Ended");
		return inventoryRepository.findBySkuCodeIn(skuCode).stream()
				.map(inventory ->
						InventoryResponse.builder()
								.skuCode(inventory.getSkuCode())
								.isInStock(inventory.getQuantity() > 0)
								.build()
				).toList();
	}
}

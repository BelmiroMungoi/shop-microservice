package com.bbm.inventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.bbm.inventoryservice.model.Inventory;
import com.bbm.inventoryservice.repository.InventoryRepository;

@EnableDiscoveryClient
@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("iphone_14");
			inventory.setQuantity(150);
			
			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("samsung_23");
			inventory2.setQuantity(0);
			
			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory2);
		};
	}

}

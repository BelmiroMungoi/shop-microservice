package com.bbm.productservice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;

import com.bbm.productservice.model.dto.ProductRequest;
import com.bbm.productservice.model.dto.ProductResponse;
import com.bbm.productservice.repository.ProductRepository;
import com.bbm.productservice.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ProductRepository productRepository;

	@MockBean
	private ProductService productService;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	@DisplayName("Deve salvar um produto quando fazer-se um POST request ao end-point - /api/product")
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		
		String productRequestString = objectMapper.writeValueAsString(productRequest); 
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
		.andExpect(status().isCreated());
		//Assertions.assertEquals(1, productRepository.findAll().size());
	}	
	
	@Test
	@DisplayName("Deve listar todos os produtos quando fazer-se um GET request ao end-point - /api/product")
	void shouldListAllProducts() throws Exception{
		ProductResponse productResponse = new ProductResponse("645753af1c5ad92d58a2bf37", "Iphone", "Iphone 14", BigDecimal.valueOf(150000));

		Mockito.when(productService.getAllProducts()).thenReturn(Arrays.asList(productResponse));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)));
	}
	
	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Xiaomi")
				.description("Xiaomi 13 Ultra")
				.price(BigDecimal.valueOf(200000))
				.build();
	}
}

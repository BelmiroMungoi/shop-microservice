package com.bbm.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.bbm.orderservice.event.OrderPlacedEvent;
import com.bbm.orderservice.model.dto.InventoryResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbm.orderservice.model.Order;
import com.bbm.orderservice.model.OrderLineItems;
import com.bbm.orderservice.model.dto.OrderLineItemsRequest;
import com.bbm.orderservice.model.dto.OrderRequest;
import com.bbm.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;

	private final WebClient.Builder webClientBuilder;

	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
	
	public String placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItems> orderLineItems = orderRequest.getItems()
			.stream()
			.map(this::mapToDto)
			.toList();
		
		order.setOrderLineItems(orderLineItems);

		List<String> skuCodes = order.getOrderLineItems().stream()
				.map(OrderLineItems::getSkuCode).toList();

		//Call the Inventory Service and place order if product is in stock
		InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory",
						uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes)
								.build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();

		boolean allProductsInStock = Arrays.stream(inventoryResponses)
				.allMatch(InventoryResponse::isInStock);

		if (allProductsInStock) {
			orderRepository.save(order);
			kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
			return "Pedido realizado com sucesso";
		} else {
			throw new IllegalArgumentException("Produto não está no estoque, TENTE NOVAMENTE mais tarde");
		}

	}

	private OrderLineItems mapToDto(OrderLineItemsRequest orderLineItemsRequest) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsRequest.getPrice());
		orderLineItems.setQuantity(orderLineItemsRequest.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsRequest.getSkuCode());
		return orderLineItems;
	}
}

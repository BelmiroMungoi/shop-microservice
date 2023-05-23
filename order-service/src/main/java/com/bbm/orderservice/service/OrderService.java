package com.bbm.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbm.orderservice.model.Order;
import com.bbm.orderservice.model.OrderLineItems;
import com.bbm.orderservice.model.dto.OrderLineItemsRequest;
import com.bbm.orderservice.model.dto.OrderRequest;
import com.bbm.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItems> orderLineItems = orderRequest.getItems()
			.stream()
			.map(orderLineItemsRequest -> mapToDto(orderLineItemsRequest))
			.toList();
		
		order.setOrderLineItems(orderLineItems);

		//Call the Inventory Service and place order if product is in stock
		orderRepository.save(order);
				
	}

	private OrderLineItems mapToDto(OrderLineItemsRequest orderLineItemsRequest) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsRequest.getPrice());
		orderLineItems.setQuantity(orderLineItemsRequest.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsRequest.getSkuCode());
		return orderLineItems;
	}
}

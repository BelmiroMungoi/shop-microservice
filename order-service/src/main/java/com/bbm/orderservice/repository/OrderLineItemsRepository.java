package com.bbm.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbm.orderservice.model.OrderLineItems;

public interface OrderLineItemsRepository extends JpaRepository<OrderLineItems, Long>{

}

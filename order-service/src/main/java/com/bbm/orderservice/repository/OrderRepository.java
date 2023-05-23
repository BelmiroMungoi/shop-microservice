package com.bbm.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbm.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}

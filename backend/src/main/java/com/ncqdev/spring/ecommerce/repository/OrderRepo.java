package com.ncqdev.spring.ecommerce.repository;

import com.ncqdev.spring.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}

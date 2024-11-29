package com.ncqdev.spring.ecommerce.repository;

import com.ncqdev.spring.ecommerce.entity.OrderItem;
import com.ncqdev.spring.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {
    Optional<OrderItem> findByProductId(Long productId);
}

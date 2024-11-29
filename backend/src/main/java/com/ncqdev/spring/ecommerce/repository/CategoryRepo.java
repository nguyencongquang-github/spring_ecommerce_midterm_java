package com.ncqdev.spring.ecommerce.repository;

import com.ncqdev.spring.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}

package com.ncqdev.spring.ecommerce.service.interf;

import com.ncqdev.spring.ecommerce.dto.CategoryDto;
import com.ncqdev.spring.ecommerce.dto.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryRequest);
    Response updateCategory(Long categoryId, CategoryDto categoryRequest);
    Response getAllCategories();
    Response getCategoryById(Long categoryId);
    Response deleteCategory(Long categoryId);
}

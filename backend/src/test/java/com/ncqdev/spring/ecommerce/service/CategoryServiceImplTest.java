package com.ncqdev.spring.ecommerce.service;


import com.ncqdev.spring.ecommerce.entity.Category;
import com.ncqdev.spring.ecommerce.exception.NotFoundException;
import com.ncqdev.spring.ecommerce.mapper.EntityDtoMapper;
import com.ncqdev.spring.ecommerce.repository.CategoryRepo;
import com.ncqdev.spring.ecommerce.dto.CategoryDto;
import com.ncqdev.spring.ecommerce.dto.Response;
import com.ncqdev.spring.ecommerce.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private EntityDtoMapper entityDtoMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock data
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Electronics");
    }

    @Test
    void testCreateCategory() {
        // Arrange
        when(categoryRepo.save(any(Category.class))).thenReturn(category);

        CategoryDto categoryRequest = new CategoryDto();
        categoryRequest.setName("Electronics");

        // Act
        Response response = categoryService.createCategory(categoryRequest);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("Category created successfully", response.getMessage());
        verify(categoryRepo, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory() {
        // Arrange
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepo.save(any(Category.class))).thenReturn(category);

        CategoryDto categoryRequest = new CategoryDto();
        categoryRequest.setName("Updated Electronics");

        // Act
        Response response = categoryService.updateCategory(1L, categoryRequest);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("category updated successfully", response.getMessage());
        verify(categoryRepo, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_NotFound() {
        // Arrange
        when(categoryRepo.findById(1L)).thenReturn(Optional.empty());

        CategoryDto categoryRequest = new CategoryDto();
        categoryRequest.setName("Updated Electronics");

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.updateCategory(1L, categoryRequest);
        });
        assertEquals("Category Not Found", exception.getMessage());
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        when(categoryRepo.findAll()).thenReturn(List.of(category));
        when(entityDtoMapper.mapCategoryToDtoBasic(any(Category.class))).thenReturn(categoryDto);

        // Act
        Response response = categoryService.getAllCategories();

        // Assert
        assertEquals(200, response.getStatus());
        assertNotNull(response.getCategoryList());
        assertEquals(1, response.getCategoryList().size());
        assertEquals("Electronics", response.getCategoryList().get(0).getName());
    }

    @Test
    void testGetCategoryById() {
        // Arrange
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(entityDtoMapper.mapCategoryToDtoBasic(any(Category.class))).thenReturn(categoryDto);

        // Act
        Response response = categoryService.getCategoryById(1L);

        // Assert
        assertEquals(200, response.getStatus());
        assertNotNull(response.getCategory());
        assertEquals("Electronics", response.getCategory().getName());
    }

    @Test
    void testGetCategoryById_NotFound() {
        // Arrange
        when(categoryRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.getCategoryById(1L);
        });
        assertEquals("Category Not Found", exception.getMessage());
    }

    @Test
    void testDeleteCategory() {
        // Arrange
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepo).delete(any(Category.class));

        // Act
        Response response = categoryService.deleteCategory(1L);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("Category was deleted successfully", response.getMessage());
        verify(categoryRepo, times(1)).delete(any(Category.class));
    }

    @Test
    void testDeleteCategory_NotFound() {
        // Arrange
        when(categoryRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.deleteCategory(1L);
        });
        assertEquals("Category Not Found", exception.getMessage());
    }
}

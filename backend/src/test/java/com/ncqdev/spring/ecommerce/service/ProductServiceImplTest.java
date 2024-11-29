package com.ncqdev.spring.ecommerce.service;

import com.ncqdev.spring.ecommerce.dto.ProductDto;
import com.ncqdev.spring.ecommerce.dto.Response;
import com.ncqdev.spring.ecommerce.entity.Category;
import com.ncqdev.spring.ecommerce.entity.OrderItem;
import com.ncqdev.spring.ecommerce.entity.Product;
import com.ncqdev.spring.ecommerce.exception.NotFoundException;
import com.ncqdev.spring.ecommerce.exception.ProductDeletionNotAllowedException;
import com.ncqdev.spring.ecommerce.mapper.EntityDtoMapper;
import com.ncqdev.spring.ecommerce.repository.CategoryRepo;
import com.ncqdev.spring.ecommerce.repository.OrderItemRepo;
import com.ncqdev.spring.ecommerce.repository.ProductRepo;
import com.ncqdev.spring.ecommerce.service.AwsS3Service;
import com.ncqdev.spring.ecommerce.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class ProductServiceImplTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private OrderItemRepo orderItemRepo;

    @Mock
    private EntityDtoMapper entityDtoMapper;

    @Mock
    private AwsS3Service awsS3Service;

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    OrderItem orderItem;

    @Test
    void testCreateProduct_Success() {
        Long categoryId = 1L;
        String name = "Product 1";
        String description = "Description";
        BigDecimal price = BigDecimal.valueOf(100.0);
        MultipartFile image = mock(MultipartFile.class);

        Category category = new Category();
        category.setId(categoryId);

        String imageUrl = "https://example.com/product1.jpg";
        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName(name);

        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        when(awsS3Service.saveImageToS3(image)).thenReturn(imageUrl);
        when(productRepo.save(any(Product.class))).thenReturn(savedProduct);

        Response response = productService.createProduct(categoryId, image, name, description, price);

        assertEquals(200, response.getStatus());
        assertEquals("Product successfully created", response.getMessage());
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(orderItemRepo.findByProductId(productId)).thenReturn(Optional.empty());

        Response response = productService.deleteProduct(productId);

        assertEquals(200, response.getStatus());
        assertEquals("Product deleted successfully", response.getMessage());
        verify(productRepo, times(1)).delete(product);
    }

    @Test
    void testDeleteProduct_ThrowsException_WhenProductIsOrdered() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(orderItemRepo.findByProductId(productId)).thenReturn(Optional.of(orderItem));

        assertThrows(ProductDeletionNotAllowedException.class, () -> productService.deleteProduct(productId));
        verify(productRepo, never()).delete(product);
    }

    @Test
    void testGetProductById_Success() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        ProductDto productDto = new ProductDto();
        productDto.setId(productId);

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(entityDtoMapper.mapProductToDtoBasic(product)).thenReturn(productDto);

        Response response = productService.getProductById(productId);

        assertEquals(200, response.getStatus());
        assertEquals(productDto, response.getProduct());
    }


    @Test
    void testGetProductsByCategory_NoProductsFound() {
        // Mock categoryRepo.existsById trả về true
        Long categoryId = 1L;
        when(categoryRepo.existsById(categoryId)).thenReturn(true);

        // Mock productRepo.findByCategoryId trả về danh sách rỗng
        when(productRepo.findByCategoryId(categoryId)).thenReturn(List.of());

        // Kiểm tra và xác minh ngoại lệ được ném ra
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> productService.getProductsByCategory(categoryId));
        assertEquals("No Products found for this category", exception.getMessage());

        // Xác minh các phương thức mock được gọi đúng
        verify(categoryRepo, times(1)).existsById(categoryId);
        verify(productRepo, times(1)).findByCategoryId(categoryId);
        verifyNoInteractions(entityDtoMapper);
    }

    @Test
    void testGetProductById_ThrowsException_WhenNotFound() {
        Long productId = 1L;
        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    void testGetProductsByCategory_Success() {
        Long categoryId = 1L;

        List<Product> products = List.of(new Product(), new Product());
        List<ProductDto> productDtos = List.of(new ProductDto(), new ProductDto());

        when(categoryRepo.existsById(categoryId)).thenReturn(true);
        when(productRepo.findByCategoryId(categoryId)).thenReturn(products);
        when(entityDtoMapper.mapProductToDtoBasic(any(Product.class))).thenReturn(productDtos.get(0), productDtos.get(1));

        Response response = productService.getProductsByCategory(categoryId);

        assertEquals(200, response.getStatus());
        assertEquals(productDtos.size(), response.getProductList().size());
    }

    @Test
    void testGetProductsByCategory_ThrowsException_WhenCategoryNotFound() {
        Long categoryId = 1L;
        when(categoryRepo.existsById(categoryId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> productService.getProductsByCategory(categoryId));
    }

    @Test
    void testSearchProduct_Success() {
        String searchValue = "Product";

        List<Product> products = List.of(new Product(), new Product());
        List<ProductDto> productDtos = List.of(new ProductDto(), new ProductDto());

        when(productRepo.findByNameContainingOrDescriptionContaining(searchValue, searchValue)).thenReturn(products);
        when(entityDtoMapper.mapProductToDtoBasic(any(Product.class))).thenReturn(productDtos.get(0), productDtos.get(1));

        Response response = productService.searchProduct(searchValue);

        assertEquals(200, response.getStatus());
        assertEquals(productDtos.size(), response.getProductList().size());
    }

    @Test
    void testSearchProduct_ThrowsException_WhenNoProductsFound() {
        String searchValue = "Nonexistent";
        when(productRepo.findByNameContainingOrDescriptionContaining(searchValue, searchValue)).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> productService.searchProduct(searchValue));
    }

    @Test
    void testUpdateProduct_Success_UpdateAllFields() {
        Long productId = 1L;
        Long categoryId = 2L;
        String newName = "Updated Product";
        String newDescription = "Updated Description";
        BigDecimal newPrice = BigDecimal.valueOf(200.0);
        MultipartFile newImage = mock(MultipartFile.class);

        Product existingProduct = new Product();
        existingProduct.setId(productId);

        Category newCategory = new Category();
        newCategory.setId(categoryId);

        String newImageUrl = "https://example.com/updated-image.jpg";

        when(productRepo.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(newCategory));
        when(newImage.isEmpty()).thenReturn(false);
        when(awsS3Service.saveImageToS3(newImage)).thenReturn(newImageUrl);

        Response response = productService.updateProduct(productId, categoryId, newImage, newName, newDescription, newPrice);

        assertEquals(200, response.getStatus());
        assertEquals("Product updated successfully", response.getMessage());
        assertEquals(newCategory, existingProduct.getCategory());
        assertEquals(newName, existingProduct.getName());
        assertEquals(newDescription, existingProduct.getDescription());
        assertEquals(newPrice, existingProduct.getPrice());
        assertEquals(newImageUrl, existingProduct.getImageUrl());

        verify(productRepo, times(1)).save(existingProduct);
    }

    @Test
    void testUpdateProduct_Success_UpdatePartialFields() {
        Long productId = 1L;
        String newName = "Partially Updated Product";

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Product");
        existingProduct.setDescription("Old Description");
        existingProduct.setPrice(BigDecimal.valueOf(100.0));

        when(productRepo.findById(productId)).thenReturn(Optional.of(existingProduct));

        Response response = productService.updateProduct(productId, null, null, newName, null, null);

        assertEquals(200, response.getStatus());
        assertEquals("Product updated successfully", response.getMessage());
        assertEquals(newName, existingProduct.getName());
        assertEquals("Old Description", existingProduct.getDescription());
        assertEquals(BigDecimal.valueOf(100.0), existingProduct.getPrice());
        assertNull(existingProduct.getImageUrl());

        verify(productRepo, times(1)).save(existingProduct);
    }

    @Test
    void testUpdateProduct_ThrowsException_ProductNotFound() {
        Long productId = 1L;

        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.updateProduct(productId, null, null, null, null, null));
        verify(productRepo, never()).save(any());
    }

    @Test
    void testUpdateProduct_ThrowsException_CategoryNotFound() {
        Long productId = 1L;
        Long categoryId = 2L;

        Product existingProduct = new Product();
        existingProduct.setId(productId);

        when(productRepo.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.updateProduct(productId, categoryId, null, null, null, null));
        verify(productRepo, never()).save(existingProduct);
    }

    @Test
    void testGetAllProducts_Success() {
        // Mock dữ liệu trả về từ ProductRepo
        List<Product> mockProducts = List.of(
                new Product(1L, "Product 1", "Description 1",  "image1.jpg", BigDecimal.valueOf(100.0),null),
                new Product(2L, "Product 2", "Description 2", "image2.jpg",  BigDecimal.valueOf(200.0), null)
        );

        List<ProductDto> mockProductDtos = List.of(
                new ProductDto(1L, "Product 1", "Description 1", BigDecimal.valueOf(100.0), "image1.jpg", null),
                new ProductDto(2L, "Product 2", "Description 2", BigDecimal.valueOf(200.0), "image2.jpg", null)
        );

        when(productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(mockProducts);
        when(entityDtoMapper.mapProductToDtoBasic(mockProducts.get(0))).thenReturn(mockProductDtos.get(0));
        when(entityDtoMapper.mapProductToDtoBasic(mockProducts.get(1))).thenReturn(mockProductDtos.get(1));

        // Gọi phương thức
        Response response = productService.getAllProducts();

        // Kiểm tra kết quả
        assertEquals(200, response.getStatus());
        assertNotNull(response.getProductList());
        assertEquals(2, response.getProductList().size());
        assertEquals("Product 1", response.getProductList().get(0).getName());
        assertEquals("Product 2", response.getProductList().get(1).getName());

        // Xác minh các phương thức mock được gọi
        verify(productRepo, times(1)).findAll(Sort.by(Sort.Direction.DESC, "id"));
        verify(entityDtoMapper, times(1)).mapProductToDtoBasic(mockProducts.get(0));
        verify(entityDtoMapper, times(1)).mapProductToDtoBasic(mockProducts.get(1));
    }

    @Test
    void testGetAllProducts_EmptyList() {
        // Mock dữ liệu trả về từ ProductRepo là danh sách rỗng
        when(productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(List.of());

        // Gọi phương thức
        Response response = productService.getAllProducts();

        // Kiểm tra kết quả
        assertEquals(200, response.getStatus());
        assertNotNull(response.getProductList());
        assertTrue(response.getProductList().isEmpty());

        // Xác minh các phương thức mock được gọi
        verify(productRepo, times(1)).findAll(Sort.by(Sort.Direction.DESC, "id"));
        verify(entityDtoMapper, never()).mapProductToDtoBasic(any());
    }

    @Test
    void testUpdateProduct_WithImage() throws Exception {
        // Mock các phụ thuộc
        Long productId = 1L;
        Long categoryId = 2L;
        MultipartFile image = mock(MultipartFile.class);
        String imageUrl = "https://mockimageurl.com";
        String name = "Updated Product";
        String description = "Updated Description";
        BigDecimal price = BigDecimal.valueOf(100.0);

        // Giả lập hành vi của awsS3Service.saveImageToS3
        when(awsS3Service.saveImageToS3(image)).thenReturn(imageUrl);

        // Mock các hành vi của productRepo và categoryRepo
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Product");
        existingProduct.setDescription("Old Description");
        existingProduct.setPrice(BigDecimal.valueOf(50.0));
        existingProduct.setCategory(new Category());
        when(productRepo.findById(productId)).thenReturn(Optional.of(existingProduct));
        Category category = new Category();
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        // Gọi phương thức cần test
        Response response = productService.updateProduct(productId, categoryId, image, name, description, price);

        // Xác minh hành vi và kết quả
        assertEquals(200, response.getStatus());
        assertEquals("Product updated successfully", response.getMessage());
        verify(awsS3Service, times(1)).saveImageToS3(image); // Kiểm tra xem awsS3Service.saveImageToS3 có được gọi
        verify(productRepo, times(1)).save(existingProduct); // Kiểm tra xem productRepo.save có được gọi

        // Kiểm tra xem các thuộc tính của sản phẩm đã được cập nhật chưa
        assertEquals(category, existingProduct.getCategory());
        assertEquals(name, existingProduct.getName());
        assertEquals(description, existingProduct.getDescription());
        assertEquals(price, existingProduct.getPrice());
        assertEquals(imageUrl, existingProduct.getImageUrl());
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        // Mock các phụ thuộc
        Long productId = 1L;

        // Giả lập hành vi của productRepo khi không tìm thấy sản phẩm
        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        // Kiểm tra xem NotFoundException có được ném ra không
        assertThrows(NotFoundException.class, () -> productService.deleteProduct(productId), "Product Not Found");
    }


}

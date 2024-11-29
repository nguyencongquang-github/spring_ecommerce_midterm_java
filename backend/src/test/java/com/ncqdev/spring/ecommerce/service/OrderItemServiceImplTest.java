package com.ncqdev.spring.ecommerce.service;

import com.ncqdev.spring.ecommerce.dto.OrderItemDto;
import com.ncqdev.spring.ecommerce.dto.OrderItemRequest;
import com.ncqdev.spring.ecommerce.dto.OrderRequest;
import com.ncqdev.spring.ecommerce.dto.Response;
import com.ncqdev.spring.ecommerce.entity.Order;
import com.ncqdev.spring.ecommerce.entity.OrderItem;
import com.ncqdev.spring.ecommerce.entity.Product;
import com.ncqdev.spring.ecommerce.entity.User;
import com.ncqdev.spring.ecommerce.enums.OrderStatus;
import com.ncqdev.spring.ecommerce.exception.NotFoundException;
import com.ncqdev.spring.ecommerce.mapper.EntityDtoMapper;
import com.ncqdev.spring.ecommerce.repository.OrderItemRepo;
import com.ncqdev.spring.ecommerce.repository.OrderRepo;
import com.ncqdev.spring.ecommerce.repository.ProductRepo;
import com.ncqdev.spring.ecommerce.service.impl.OrderItemServiceImpl;
import com.ncqdev.spring.ecommerce.service.interf.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private OrderItemRepo orderItemRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserService userService;

    @Mock
    private EntityDtoMapper entityDtoMapper;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Test
    void placeOrder_success() {
        // Arrange
        User mockUser = new User();
        mockUser.setId(1L);

        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setPrice(BigDecimal.valueOf(100));

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItems(List.of(new OrderItemRequest(1L, 2)));
        orderRequest.setTotalPrice(BigDecimal.valueOf(200));

        Mockito.when(userService.getLoginUser()).thenReturn(mockUser);
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(mockProduct));

        // Act
        Response response = orderItemService.placeOrder(orderRequest);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("Order was successfully placed", response.getMessage());
        Mockito.verify(orderRepo).save(Mockito.any(Order.class));
    }

    @Test
    void placeOrder_productNotFound() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItems(List.of(new OrderItemRequest(99L, 2)));

        Mockito.when(userService.getLoginUser()).thenReturn(new User());
        Mockito.when(productRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> orderItemService.placeOrder(orderRequest));
    }

    @Test
    void updateOrderItemStatus_success() {
        // Arrange
        OrderItem mockOrderItem = new OrderItem();
        mockOrderItem.setId(1L);
        mockOrderItem.setStatus(OrderStatus.PENDING);

        Mockito.when(orderItemRepo.findById(1L)).thenReturn(Optional.of(mockOrderItem));

        // Act
        Response response = orderItemService.updateOrderItemStatus(1L, "CONFIRMED");

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("Order status updated successfully", response.getMessage());
        assertEquals(OrderStatus.CONFIRMED, mockOrderItem.getStatus());
        Mockito.verify(orderItemRepo).save(mockOrderItem);
    }

    @Test
    void updateOrderItemStatus_notFound() {
        // Arrange
        Mockito.when(orderItemRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> orderItemService.updateOrderItemStatus(1L, "COMPLETED"));
    }

    @Test
    void filterOrderItems_success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        OrderItem mockOrderItem = new OrderItem();
        mockOrderItem.setId(1L);

        Page<OrderItem> mockPage = new PageImpl<>(List.of(mockOrderItem), pageable, 1);

        Mockito.when(orderItemRepo.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(mockPage);

        Mockito.when(entityDtoMapper.mapOrderItemToDtoPlusProductAndUser(Mockito.any()))
                .thenReturn(new OrderItemDto());

        // Act
        Response response = orderItemService.filterOrderItems(OrderStatus.PENDING, null, null, null, pageable);

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals(1, response.getOrderItemList().size());
    }

    @Test
    void filterOrderItems_notFound() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(orderItemRepo.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(Page.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () ->
                orderItemService.filterOrderItems(OrderStatus.PENDING, null, null, null, pageable)
        );
    }





}


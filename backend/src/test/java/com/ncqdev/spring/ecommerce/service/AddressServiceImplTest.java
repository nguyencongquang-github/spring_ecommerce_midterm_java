//package com.ncqdev.spring.ecommerce.service;
//
//import com.ncqdev.spring.ecommerce.dto.AddressDto;
//import com.ncqdev.spring.ecommerce.dto.Response;
//import com.ncqdev.spring.ecommerce.entity.Address;
//import com.ncqdev.spring.ecommerce.entity.User;
//import com.ncqdev.spring.ecommerce.repository.AddressRepo;
//import com.ncqdev.spring.ecommerce.service.impl.AddressServiceImpl;
//import com.ncqdev.spring.ecommerce.service.interf.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@TestPropertySource("/test.properties")
//class AddressServiceImplTest {
//
//    @Mock
//    private AddressRepo addressRepo;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private AddressServiceImpl addressServiceImpl;
//
//    private User user;
//    private AddressDto addressDto;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Setup test data
//        user = new User();
//        user.setId(1L);
//        user.setName("Test User");
//        user.setEmail("testuser@example.com");
//
//        addressDto = new AddressDto();
//        addressDto.setStreet("123 Main St");
//        addressDto.setCity("Test City");
//        addressDto.setState("Test State");
//        addressDto.setZipCode("12345");
//        addressDto.setCountry("Test Country");
//    }
//
//    @Test
//    void testSaveAndUpdateAddress_whenUserNotFound() {
//        // Mock the behavior of userService.getLoginUser() to return null
//        when(userService.getLoginUser()).thenReturn(null);
//
//        Response response = addressServiceImpl.saveAndUpdateAddress(addressDto);
//
//        assertEquals(400, response.getStatus());
//        assertEquals("User not found", response.getMessage());
//    }
//
//    @Test
//    void testSaveAndUpdateAddress_whenAddressIsNull() {
//        // Mock the behavior of userService.getLoginUser() to return a user with no address
//        when(userService.getLoginUser()).thenReturn(user);
//        when(addressRepo.save(any(Address.class))).thenReturn(new Address());
//
//        Response response = addressServiceImpl.saveAndUpdateAddress(addressDto);
//
//        assertEquals(200, response.getStatus());
//        assertEquals("Address successfully created", response.getMessage());
//
//        // Verify the interaction with the addressRepo.save method
//        verify(addressRepo, times(1)).save(any(Address.class));
//    }
//
//    @Test
//    void testSaveAndUpdateAddress_whenAddressExists() {
//        // Create an existing address for the user
//        Address existingAddress = new Address();
//        existingAddress.setUser(user);
//        user.setAddress(existingAddress);
//
//        // Mock the behavior of userService.getLoginUser() to return a user with an existing address
//        when(userService.getLoginUser()).thenReturn(user);
//        when(addressRepo.save(any(Address.class))).thenReturn(existingAddress);
//
//        Response response = addressServiceImpl.saveAndUpdateAddress(addressDto);
//
//        assertEquals(200, response.getStatus());
//        assertEquals("Address successfully updated", response.getMessage());
//
//        // Verify the interaction with the addressRepo.save method
//        verify(addressRepo, times(1)).save(any(Address.class));
//    }
//}

//package com.ncqdev.spring.ecommerce.controller;
//
//import com.ncqdev.spring.ecommerce.dto.Response;
//import com.ncqdev.spring.ecommerce.service.interf.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Slf4j
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestPropertySource("/test.properties")
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    private Response response;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    void testGetAllUsers_whenUserIsAdmin() throws Exception {
//        // Setup mock response from userService
//        Response mockResponse = Response.builder()
//                .status(200)
//                .message("Users retrieved successfully")
//                .build();
//        when(userService.getAllUsers()).thenReturn(mockResponse);
//
//        // Mock authentication as admin
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Perform GET request
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/user/get-all")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect((ResultMatcher) jsonPath("$.status").value(200))
//                .andExpect((ResultMatcher) jsonPath("$.message").value("Users retrieved successfully"));
//
//        // Verify that the service method was called once
//        verify(userService, times(1)).getAllUsers();
//    }
//
//    @Test
//    void testGetAllUsers_whenUserIsNotAdmin() throws Exception {
//        // Mock authentication as non-admin
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Perform GET request and expect forbidden (403)
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/user/get-all")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//
//        // Verify that the service method was never called
//        verify(userService, never()).getAllUsers();
//    }
//
//    @Test
//    void testGetUserInfoAndOrderHistory() throws Exception {
//        // Setup mock response from userService
//        Response mockResponse = Response.builder()
//                .status(200)
//                .message("User info and order history retrieved successfully")
//                .build();
//        when(userService.getUserInfoAndOrderHistory()).thenReturn(mockResponse);
//
//        // Perform GET request
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/user/my-info")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect((ResultMatcher) jsonPath("$.status").value(200))
//                .andExpect((ResultMatcher) jsonPath("$.message").value("User info and order history retrieved successfully"));
//
//        // Verify that the service method was called once
//        verify(userService, times(1)).getUserInfoAndOrderHistory();
//    }
//
//
//}

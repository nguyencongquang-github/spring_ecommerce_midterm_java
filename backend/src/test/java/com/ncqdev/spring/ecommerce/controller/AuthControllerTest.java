package com.ncqdev.spring.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncqdev.spring.ecommerce.dto.LoginRequest;
import com.ncqdev.spring.ecommerce.dto.Response;
import com.ncqdev.spring.ecommerce.dto.UserDto;
import com.ncqdev.spring.ecommerce.service.interf.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDto request;
    private UserDto response;
    private LoginRequest loginRequest;

    @BeforeEach
    public void initData() {
        request = UserDto.builder()
                .email("nguyencongquang2k4@gmail.com")
                .name("Nguyen Cong Quang")
                .phoneNumber("0389171890")
                .password("123456")
                .build();

        loginRequest = LoginRequest.builder()
                .email("nguyencongquang2k4@gmail.com")
                .password("123456")
                .build();


        response = UserDto.builder()
                .id(4L)
                .email("nguyencongquang2k4@gmail.com")
                .name("Nguyen Cong Quang")
                .phoneNumber("0389171890")
                .password("123456")
                .role("USER")
                .build();
    }

    @Test
    public void registerUser_validRequest_success() throws Exception {
        log.info("Controller: Test register user");

        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.registerUser(ArgumentMatchers.any())).thenReturn(Response.builder()
                .status(200)
                .message("User Successfully Added")
                .user(response)
                .build());

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("user.id").value(4))
                .andExpect(jsonPath("user.email").value("nguyencongquang2k4@gmail.com"))
                .andExpect(jsonPath("user.name").value("Nguyen Cong Quang"))
                .andExpect(jsonPath("user.phoneNumber").value("0389171890"))
                .andExpect(jsonPath("user.role").value("USER"))
                .andExpect(jsonPath("message").value("User Successfully Added"));
    }

    @Test
    public void registerUser_usernameInvalid_fail() throws Exception {
        log.info("Controller: Test register user with invalid username");

        // GIVEN
        request.setName("nc");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);


        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("Name must be at least 3 characters"));
    }

    @Test
    public void registerUser_emailInvalid_fail() throws Exception {
        log.info("Controller: Test register user with invalid email");

        // GIVEN
        request.setEmail("nguyencongquang.com");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("Email is not valid"));
    }

    @Test
    public void registerUser_phoneNumberInvalid_fail() throws Exception {
        log.info("Controller: Test register user with invalid phone number");

        // GIVEN
        request.setPhoneNumber("038917189");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("Phone number must be at least 10 characters"));
    }


    @Test
    public void registerUser_passwordInvalid_fail() throws Exception {
        log.info("Controller: Test register user with invalid password");

        // GIVEN
        request.setPassword("123");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("Password must be at least 6 characters"));
    }


   // Test login
   @Test
   public void loginUser_validRequest_success() throws Exception {
       ObjectMapper objectMapper = new ObjectMapper();
       String content = objectMapper.writeValueAsString(loginRequest);

       Mockito.when(userService.loginUser(ArgumentMatchers.any())).thenReturn(
               Response.builder()
                       .status(200)
                       .message("User Successfully Logged In")
                       .build()
       );

       mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                       .post("/auth/login")
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .content(content))
               .andExpect(status().isOk())
               .andExpect(jsonPath("status").value(200))
               .andExpect(jsonPath("message").value("User Successfully Logged In"));
   }

    @Test
    public void loginUser_invalidCredentials_fail() throws Exception {
        loginRequest.setPassword("");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(loginRequest);

        Mockito.when(userService.loginUser(ArgumentMatchers.any())).thenReturn(
                Response.builder()
                        .status(400)
                        .message("Password is required")
                        .build()
        );

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("Password is required"));
    }
}

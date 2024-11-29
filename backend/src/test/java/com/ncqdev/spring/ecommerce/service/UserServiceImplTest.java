package com.ncqdev.spring.ecommerce.service;

import com.ncqdev.spring.ecommerce.dto.LoginRequest;
import com.ncqdev.spring.ecommerce.dto.Response;
import com.ncqdev.spring.ecommerce.dto.UserDto;
import com.ncqdev.spring.ecommerce.entity.User;
import com.ncqdev.spring.ecommerce.enums.UserRole;
import com.ncqdev.spring.ecommerce.exception.InvalidCredentialsException;
import com.ncqdev.spring.ecommerce.exception.NotFoundException;
import com.ncqdev.spring.ecommerce.mapper.EntityDtoMapper;
import com.ncqdev.spring.ecommerce.repository.UserRepo;
import com.ncqdev.spring.ecommerce.security.JwtUtils;
import com.ncqdev.spring.ecommerce.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private EntityDtoMapper entityDtoMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        // Arrange
        UserDto userDto = UserDto.builder()
                .email("test@example.com")
                .name("Test User")
                .password("password123")
                .phoneNumber("1234567890")
                .build();

        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        User user = User.builder()
                .email("test@example.com")
                .name("Test User")
                .password("encodedPassword")
                .phoneNumber("1234567890")
                .role(UserRole.USER)
                .build();
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(entityDtoMapper.mapUserToDtoBasic(user)).thenReturn(userDto);

        // Act
        Response response = userService.registerUser(userDto);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("User Successfully Added", response.getMessage());
        assertEquals(userDto.getEmail(), response.getUser().getEmail());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        // Arrange
        UserDto userDto = UserDto.builder()
                .email("test@example.com")
                .build();
        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class,
                () -> userService.registerUser(userDto));
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void loginUser_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
        User user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role(UserRole.USER)
                .build();

        when(userRepo.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("mockToken");

        // Act
        Response response = userService.loginUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("mockToken", response.getToken());
        verify(userRepo, times(1)).findByEmail(loginRequest.getEmail());
    }

    @Test
    void loginUser_InvalidPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongPassword");
        User user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .build();

        when(userRepo.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class,
                () -> userService.loginUser(loginRequest));
        assertEquals("Password does not match", exception.getMessage());
        verify(jwtUtils, never()).generateToken(any(User.class));
    }

    @Test
    void loginUser_EmailNotFound() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("notfound@example.com", "password123");
        when(userRepo.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.loginUser(loginRequest));
        assertEquals("Email not found", exception.getMessage());
    }

    @Test
    void getAllUsers_Success() {
        // Arrange
        User user1 = User.builder()
                .id(1L)
                .name("User One")
                .email("user1@example.com")
                .phoneNumber("1234567890")
                .build();
        User user2 = User.builder()
                .id(2L)
                .name("User Two")
                .email("user2@example.com")
                .phoneNumber("0987654321")
                .build();

        List <User> users = List.of(user1, user2);

        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .name("User One")
                .email("user1@example.com")
                .phoneNumber("1234567890")
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(2L)
                .name("User Two")
                .email("user2@example.com")
                .phoneNumber("0987654321")
                .build();

        when(userRepo.findAll()).thenReturn(users);
        when(entityDtoMapper.mapUserToDtoBasic(user1)).thenReturn(userDto1);
        when(entityDtoMapper.mapUserToDtoBasic(user2)).thenReturn(userDto2);

        // Act
        Response response = userService.getAllUsers();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getUserList());
        assertEquals(2, response.getUserList().size());
        assertEquals("User One", response.getUserList().get(0).getName());
        assertEquals("User Two", response.getUserList().get(1).getName());
        verify(userRepo, times(1)).findAll();
    }

    @Test
    void getLoginUser_Success() {
        // Arrange
        String email = "user@example.com";
        User user = User.builder()
                .email(email)
                .name("Logged In User")
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.getName()).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User loggedInUser = userService.getLoginUser();

        // Assert
        assertNotNull(loggedInUser);
        assertEquals(email, loggedInUser.getEmail());
        verify(userRepo, times(1)).findByEmail(email);
    }

    @Test
    void getLoginUser_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.getName()).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.getLoginUser());
        assertEquals("User Not found", exception.getMessage());
    }

    @Test
    void getUserInfoAndOrderHistory_Success() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User With History")
                .email("historyuser@example.com")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("User With History")
                .email("historyuser@example.com")
                .build();

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user)).thenReturn(userDto);

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(user.getEmail());

        // Act
        Response response = userService.getUserInfoAndOrderHistory();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNotNull(response.getUser());
        assertEquals("User With History", response.getUser().getName());
        verify(userRepo, times(1)).findByEmail(user.getEmail());
        verify(entityDtoMapper, times(1)).mapUserToDtoPlusAddressAndOrderHistory(user);
    }

}

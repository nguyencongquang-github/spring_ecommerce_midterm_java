package com.ncqdev.spring.ecommerce.service.interf;

import com.ncqdev.spring.ecommerce.dto.LoginRequest;
import com.ncqdev.spring.ecommerce.dto.Response;
import com.ncqdev.spring.ecommerce.dto.UserDto;
import com.ncqdev.spring.ecommerce.entity.User;

public interface UserService {
    Response registerUser(UserDto registrationRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    User getLoginUser();
    Response getUserInfoAndOrderHistory();
}

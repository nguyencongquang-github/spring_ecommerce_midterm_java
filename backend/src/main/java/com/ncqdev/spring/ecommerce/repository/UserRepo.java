package com.ncqdev.spring.ecommerce.repository;

import com.ncqdev.spring.ecommerce.entity.User;
import com.ncqdev.spring.ecommerce.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByRole(UserRole userRole);
}

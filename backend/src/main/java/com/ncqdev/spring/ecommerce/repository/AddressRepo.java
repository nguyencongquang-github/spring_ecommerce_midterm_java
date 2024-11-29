package com.ncqdev.spring.ecommerce.repository;

import com.ncqdev.spring.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
}

package com.ncqdev.spring.ecommerce.service.interf;

import com.ncqdev.spring.ecommerce.dto.AddressDto;
import com.ncqdev.spring.ecommerce.dto.Response;

public interface AddressService {
    Response saveAndUpdateAddress(AddressDto addressDto);
}

package com.ncqdev.spring.ecommerce.service.impl;

import com.ncqdev.spring.ecommerce.entity.Address;
import com.ncqdev.spring.ecommerce.entity.User;
import com.ncqdev.spring.ecommerce.repository.AddressRepo;
import com.ncqdev.spring.ecommerce.service.interf.AddressService;
import com.ncqdev.spring.ecommerce.service.interf.UserService;
import com.ncqdev.spring.ecommerce.dto.AddressDto;
import com.ncqdev.spring.ecommerce.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final UserService userService;


    @Override
    public Response saveAndUpdateAddress(AddressDto addressDto) {
        User user = userService.getLoginUser();
        if (user == null) {
            return Response.builder()
                    .status(400)
                    .message("User not found")
                    .build();
        }
        
        Address address = user.getAddress();

        if (address == null){
            address = new Address();
            address.setUser(user);
        }
        if (addressDto.getStreet() != null) address.setStreet(addressDto.getStreet());
        if (addressDto.getCity() != null) address.setCity(addressDto.getCity());
        if (addressDto.getState() != null) address.setState(addressDto.getState());
        if (addressDto.getZipCode() != null) address.setZipCode(addressDto.getZipCode());
        if (addressDto.getCountry() != null) address.setCountry(addressDto.getCountry());

        addressRepo.save(address);

        String message = (user.getAddress() == null) ? "Address successfully created" : "Address successfully updated";
        return Response.builder()
                .status(200)
                .message(message)
                .build();
    }
}

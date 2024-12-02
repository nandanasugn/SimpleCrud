package com.nandana.simplecrud.service;

import com.nandana.simplecrud.dto.response.CommonResponseDto;
import com.nandana.simplecrud.dto.response.UserResponseDto;
import com.nandana.simplecrud.model.UserDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IUserDetailService {

    UserDetail getUserDetailByEmail(String email);

    Page<UserResponseDto> getCustomerDetailsByName(String name, Pageable pageable);

    Page<UserResponseDto> getAdminDetailsByName(String name, Pageable pageable);

    Page<UserResponseDto> getAllCustomerDetails(Pageable pageable);

    Page<UserResponseDto> getAllAdminDetails(Pageable pageable);

    void createUserDetail(UserDetail userDetail);

    void updateUserDetail(UserDetail userDetail);

    ResponseEntity<CommonResponseDto<?>> deleteCustomer(String email);

    ResponseEntity<CommonResponseDto<?>> deleteAdmin(String email);
}

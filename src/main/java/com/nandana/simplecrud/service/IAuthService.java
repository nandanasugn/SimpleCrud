package com.nandana.simplecrud.service;

import com.nandana.simplecrud.dto.request.ChangePasswordRequestDto;
import com.nandana.simplecrud.dto.request.LoginRequestDto;
import com.nandana.simplecrud.dto.request.RegisterRequestDto;
import com.nandana.simplecrud.dto.request.UserUpdateRequestDto;
import com.nandana.simplecrud.dto.response.CommonResponseDto;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<CommonResponseDto<?>> registerCustomer(RegisterRequestDto registerRequestDto);

    ResponseEntity<CommonResponseDto<?>> registerAdmin(RegisterRequestDto registerRequestDto);

    ResponseEntity<CommonResponseDto<?>> login(LoginRequestDto loginRequestDto)
            ;

    ResponseEntity<CommonResponseDto<?>> changePassword(ChangePasswordRequestDto changePasswordRequestDto);

    ResponseEntity<CommonResponseDto<?>> updateCustomer(String email, UserUpdateRequestDto userUpdateRequestDto);

    ResponseEntity<CommonResponseDto<?>> updateAdmin(String email, UserUpdateRequestDto userUpdateRequestDto);
}

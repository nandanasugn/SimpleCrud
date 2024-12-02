package com.nandana.simplecrud.mapper;

import com.nandana.simplecrud.dto.response.CommonResponseDto;
import com.nandana.simplecrud.dto.response.ErrorResponseDto;
import com.nandana.simplecrud.dto.response.LoginResponseDto;
import com.nandana.simplecrud.dto.response.UserResponseDto;
import com.nandana.simplecrud.model.User;
import com.nandana.simplecrud.model.UserDetail;
import com.nandana.simplecrud.model.enums.EUserRole;
import org.springframework.http.HttpStatus;

import java.sql.Date;

public class DtoMapper {
    public static CommonResponseDto<?> commonResponseDto(String message, HttpStatus status, Object data) {
        return CommonResponseDto.builder()
                .message(message)
                .status(status.value())
                .data(data)
                .build();
    }

    public static ErrorResponseDto errorResponseDto(String timestamp, HttpStatus status, String error, String message, String path) {
        return ErrorResponseDto.builder()
                .timestamp(timestamp)
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .build();
    }

    public static User user(String username, String password, EUserRole role) {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }

    public static UserDetail userDetail(String firstName, String middleName, String lastName, String email, String phoneNumber, String address, Date dateOfBirth, User user) {
        return UserDetail.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .user(user)
                .build();
    }

    public static LoginResponseDto loginResponseDto(String username, String role, String token) {
        return LoginResponseDto.builder()
                .username(username)
                .role(role)
                .token(token)
                .build();
    }

    public static UserResponseDto userResponseDto(String firstName, String middleName, String lastName, String username, String email, String phoneNumber, String address, Date dateOfBirth, String imageUrl, String videoUrl, String documentUrl) {
        return UserResponseDto.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .username(username)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .imageUrl(imageUrl)
                .videoUrl(videoUrl)
                .documentUrl(documentUrl)
                .build();
    }
}

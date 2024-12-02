package com.nandana.simplecrud.controller;

import com.nandana.simplecrud.dto.request.ChangePasswordRequestDto;
import com.nandana.simplecrud.dto.request.LoginRequestDto;
import com.nandana.simplecrud.dto.request.RegisterRequestDto;
import com.nandana.simplecrud.dto.request.UserUpdateRequestDto;
import com.nandana.simplecrud.dto.response.CommonResponseDto;
import com.nandana.simplecrud.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Management", description = "Endpoints for managing authentications, such as registration, login, and user updates.")
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register/customer")
    @Operation(
            summary = "Register a new customer",
            description = "This endpoint registers a new customer in the system. The customer's details should be provided in the request.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer registration details"),
            tags = {"Authentication Management"}
    )
    public ResponseEntity<CommonResponseDto<?>> registerCustomer(@ModelAttribute RegisterRequestDto registerRequestDto) {
        return authService.registerCustomer(registerRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/admin")
    @Operation(
            summary = "Register a new admin",
            description = "This endpoint allows an ADMIN to register another admin. The admin details should be provided in the request.",
            security = @SecurityRequirement(name = "bearerAuth"),
            tags = {"Authentication Management"}
    )
    public ResponseEntity<CommonResponseDto<?>> registerAdmin(@ModelAttribute RegisterRequestDto registerRequestDto) {
        return authService.registerAdmin(registerRequestDto);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login a user",
            description = "This endpoint authenticates a user and returns a token for subsequent requests.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User login credentials"),
            tags = {"Authentication Management"}
    )
    public ResponseEntity<CommonResponseDto<?>> login(@ModelAttribute LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @PatchMapping("/customer")
    @Operation(
            summary = "Update customer information",
            description = "This endpoint allows a CUSTOMER or ADMIN to update customer information by providing the user's email and updated details.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = @Parameter(name = "user_email", description = "Email of the customer to update"),
            tags = {"Authentication Management"}
    )
    public ResponseEntity<CommonResponseDto<?>> updateUser(@RequestParam("user_email") String email, @ModelAttribute UserUpdateRequestDto userUpdateRequestDto) {
        return authService.updateCustomer(email, userUpdateRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin")
    @Operation(
            summary = "Update admin information",
            description = "This endpoint allows an ADMIN to update admin information by providing the user's email and updated details.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = @Parameter(name = "user_email", description = "Email of the admin to update"),
            tags = {"Authentication Management"}
    )
    public ResponseEntity<CommonResponseDto<?>> updateAdmin(@RequestParam("user_email") String email, @ModelAttribute UserUpdateRequestDto userUpdateRequestDto) {
        return authService.updateAdmin(email, userUpdateRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @PostMapping("/change-password")
    @Operation(
            summary = "Change user password",
            description = "This endpoint allows a user (ADMIN or CUSTOMER) to change their password. The old and new passwords should be provided in the request.",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Change password request details"),
            tags = {"Authentication Management"}
    )
    public ResponseEntity<CommonResponseDto<?>> changePassword(@ModelAttribute ChangePasswordRequestDto changePasswordRequestDto) {
        return authService.changePassword(changePasswordRequestDto);
    }
}

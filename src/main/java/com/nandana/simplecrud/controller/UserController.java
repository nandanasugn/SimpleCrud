package com.nandana.simplecrud.controller;

import com.nandana.simplecrud.dto.response.CommonResponseDto;
import com.nandana.simplecrud.dto.response.UserResponseDto;
import com.nandana.simplecrud.mapper.DtoMapper;
import com.nandana.simplecrud.model.UserDetail;
import com.nandana.simplecrud.service.IUserDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing user data")
public class UserController {
    private final IUserDetailService userDetailService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customer")
    @Operation(
            summary = "Retrieve all customers",
            description = "Fetch a paginated list of all customers. Filters by name if provided.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "name", description = "Optional name filter for customers"),
                    @Parameter(name = "page", description = "Page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "Number of records per page", example = "10")
            },
            tags = {"User Management"}
    )
    public ResponseEntity<CommonResponseDto<?>> getAllCustomer(@RequestParam(value = "name", required = false) String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponseDto> userResponse;
        if (name == null || name.isEmpty()) {
            userResponse = userDetailService.getAllCustomerDetails(pageable);
        } else {
            userResponse = userDetailService.getCustomerDetailsByName(name, pageable);
        }

        CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.OK, userResponse);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/customer-detail")
    @Operation(
            summary = "Get customer details by email",
            description = "Retrieve detailed information about a customer by their email address.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "email", description = "Customer's email address", required = true)
            },
            tags = {"User Management"}
    )
    public ResponseEntity<CommonResponseDto<?>> getCustomerByEmail(@RequestParam(value = "email") String email) {
        UserDetail userDetail = userDetailService.getUserDetailByEmail(email);
        UserResponseDto userResponseDto = userDetail.convertToResponseDto();
        CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.OK, userResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    @Operation(
            summary = "Retrieve all admins",
            description = "Fetch a paginated list of all administrators. Filters by name if provided.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "name", description = "Optional name filter for admins"),
                    @Parameter(name = "page", description = "Page number for pagination", example = "0"),
                    @Parameter(name = "size", description = "Number of records per page", example = "10")
            },
            tags = {"User Management"}
    )
    public ResponseEntity<CommonResponseDto<?>> getAllAdmin(@RequestParam(value = "name", required = false) String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponseDto> userResponse;
        if (name == null || name.isEmpty()) {
            userResponse = userDetailService.getAllAdminDetails(pageable);
        } else {
            userResponse = userDetailService.getAdminDetailsByName(name, pageable);
        }

        CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.OK, userResponse);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @DeleteMapping("/customer")
    @Operation(
            summary = "Delete a customer",
            description = "Delete a customer's record by their email address. Requires ADMIN or CUSTOMER role.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "email", description = "Customer's email address", required = true)
            },
            tags = {"User Management"}
    )
    public ResponseEntity<CommonResponseDto<?>> deleteCustomer(@RequestParam(value = "email") String email) {
        return userDetailService.deleteCustomer(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin")
    @Operation(
            summary = "Delete an admin",
            description = "Delete an administrator's record by their email address. Requires ADMIN role.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "email", description = "Admin's email address", required = true)
            },
            tags = {"User Management"}
    )
    public ResponseEntity<CommonResponseDto<?>> deleteAdmin(@RequestParam(value = "email") String email) {
        return userDetailService.deleteAdmin(email);
    }
}

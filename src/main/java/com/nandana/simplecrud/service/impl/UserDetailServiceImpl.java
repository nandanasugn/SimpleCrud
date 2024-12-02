package com.nandana.simplecrud.service.impl;

import com.nandana.simplecrud.dto.response.CommonResponseDto;
import com.nandana.simplecrud.dto.response.UserResponseDto;
import com.nandana.simplecrud.mapper.DtoMapper;
import com.nandana.simplecrud.model.UserDetail;
import com.nandana.simplecrud.model.enums.EUserRole;
import com.nandana.simplecrud.repository.IUserDetailRepository;
import com.nandana.simplecrud.service.IUserDetailService;
import com.nandana.simplecrud.util.ObjectValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class UserDetailServiceImpl implements IUserDetailService {
    private final ObjectValidator objectValidator;
    private final IUserDetailRepository userDetailRepository;
    private final UserServiceImpl userServiceImpl;

    @Override
    public UserDetail getUserDetailByEmail(String email) {
        return userDetailRepository.findUserDetailByEmail(email);
    }

    @Override
    public Page<UserResponseDto> getCustomerDetailsByName(String name, Pageable pageable) {
        Page<UserDetail> userDetails = userDetailRepository.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndUser_RoleOrderByCreatedDateDesc(name, name, EUserRole.CUSTOMER, pageable);
        return userDetails.map(this::convertUserDetailToResponseDTO);
    }

    @Override
    public Page<UserResponseDto> getAdminDetailsByName(String name, Pageable pageable) {
        Page<UserDetail> userDetails = userDetailRepository.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndUser_RoleOrderByCreatedDateDesc(name, name, EUserRole.ADMIN, pageable);
        return userDetails.map(this::convertUserDetailToResponseDTO);
    }

    @Override
    public Page<UserResponseDto> getAllCustomerDetails(Pageable pageable) {
        Page<UserDetail> userDetails = userDetailRepository.findUserDetailsByUser_RoleOrderByCreatedDateDesc(EUserRole.CUSTOMER, pageable);
        return userDetails.map(this::convertUserDetailToResponseDTO);
    }

    @Override
    public Page<UserResponseDto> getAllAdminDetails(Pageable pageable) {
        Page<UserDetail> userDetails = userDetailRepository.findUserDetailsByUser_RoleOrderByCreatedDateDesc(EUserRole.ADMIN, pageable);
        return userDetails.map(this::convertUserDetailToResponseDTO);
    }

    private UserResponseDto convertUserDetailToResponseDTO(UserDetail userDetail) {
        return DtoMapper.userResponseDto(userDetail.getFirstName(), userDetail.getMiddleName(), userDetail.getLastName(), userDetail.getUser().getUsername(), userDetail.getEmail(), userDetail.getPhoneNumber(), userDetail.getAddress(), userDetail.getDateOfBirth(), userDetail.getImageUrl(), userDetail.getVideoUrl(), userDetail.getDocumentUrl());
    }

    @Override
    public void createUserDetail(UserDetail userDetail) {
        objectValidator.validate(userDetail);

        try {
            userDetailRepository.save(userDetail);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error saving user detail");
        }
    }

    @Override
    public void updateUserDetail(UserDetail userDetail) {
        objectValidator.validate(userDetail);

        try {
            userDetailRepository.save(userDetail);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error updating user detail");
        }
    }

    @Override
    public ResponseEntity<CommonResponseDto<?>> deleteCustomer(String email) {
        UserDetail userDetail = userDetailRepository.findUserDetailByEmail(email);
        if (userDetail == null || userDetail.getUser().getRole() != EUserRole.CUSTOMER) {
            throw new RuntimeException("User detail not found");
        }
        userServiceImpl.deleteCustomer(userDetail.getUser().getUsername());
        userDetailRepository.delete(userDetail);


        CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.OK, "Customer deleted successfully");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CommonResponseDto<?>> deleteAdmin(String email) {
        UserDetail userDetail = userDetailRepository.findUserDetailByEmail(email);
        if (userDetail == null || userDetail.getUser().getRole() != EUserRole.ADMIN) {
            throw new RuntimeException("User detail not found");
        }
        userServiceImpl.deleteAdmin(userDetail.getUser().getUsername());
        userDetailRepository.delete(userDetail);

        CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.OK, "Admin deleted successfully");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}

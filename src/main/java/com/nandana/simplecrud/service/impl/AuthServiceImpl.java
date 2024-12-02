package com.nandana.simplecrud.service.impl;

import com.nandana.simplecrud.dto.request.ChangePasswordRequestDto;
import com.nandana.simplecrud.dto.request.LoginRequestDto;
import com.nandana.simplecrud.dto.request.RegisterRequestDto;
import com.nandana.simplecrud.dto.request.UserUpdateRequestDto;
import com.nandana.simplecrud.dto.response.CommonResponseDto;
import com.nandana.simplecrud.dto.response.LoginResponseDto;
import com.nandana.simplecrud.mapper.DtoMapper;
import com.nandana.simplecrud.model.User;
import com.nandana.simplecrud.model.UserDetail;
import com.nandana.simplecrud.model.enums.EUserRole;
import com.nandana.simplecrud.service.FileStorageService;
import com.nandana.simplecrud.service.IAuthService;
import com.nandana.simplecrud.util.JwtUtil;
import com.nandana.simplecrud.util.ObjectValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class AuthServiceImpl implements IAuthService {
    private final UserServiceImpl userService;
    private final UserDetailServiceImpl userDetailService;
    private final ObjectValidator objectValidator;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Override
    public ResponseEntity<CommonResponseDto<?>> registerCustomer(RegisterRequestDto registerRequestDto) {
        objectValidator.validate(registerRequestDto);

        User user = DtoMapper.user(registerRequestDto.getUsername(), passwordEncoder.encode(registerRequestDto.getPassword()), EUserRole.CUSTOMER);
        user.setRole(EUserRole.CUSTOMER);

        try {
            createUser(registerRequestDto, user);

            CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.CREATED, "Customer registered successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            getUserDetail(user);
            throw new RuntimeException("Error registering customer", e);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDto<?>> registerAdmin(RegisterRequestDto registerRequestDto) {
        objectValidator.validate(registerRequestDto);

        User user = DtoMapper.user(registerRequestDto.getUsername(), passwordEncoder.encode(registerRequestDto.getPassword()), EUserRole.CUSTOMER);
        user.setRole(EUserRole.ADMIN);
        try {
            createUser(registerRequestDto, user);

            CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.CREATED, "Admin registered successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            getUserDetail(user);
            throw new RuntimeException("Error registering admin", e);
        }
    }

    private void getUserDetail(User user) {
        if (user.getUserDetail() != null) {
            if (user.getUserDetail().getDocumentUrl() != null) {
                fileStorageService.deleteFile(user.getUserDetail().getDocumentUrl());
            }
            if (user.getUserDetail().getVideoUrl() != null) {
                fileStorageService.deleteFile(user.getUserDetail().getVideoUrl());
            }
            if (user.getUserDetail().getImageUrl() != null) {
                fileStorageService.deleteFile(user.getUserDetail().getImageUrl());
            }
        }
    }

    private void createUser(RegisterRequestDto registerRequestDto, User user) {
        userService.createUser(user);
        UserDetail userDetail = DtoMapper.userDetail(registerRequestDto.getFirstName(), registerRequestDto.getMiddleName(), registerRequestDto.getLastName(), registerRequestDto.getEmail(), registerRequestDto.getPhoneNumber(), registerRequestDto.getAddress(), registerRequestDto.getDateOfBirth(), user);

        if (registerRequestDto.getNationalIdDocument() != null) {
            userDetail.setDocumentUrl(fileStorageService.storeFile(registerRequestDto.getNationalIdDocument(), "documents"));
        }
        if (registerRequestDto.getSelfIntroVideo() != null) {
            userDetail.setVideoUrl(fileStorageService.storeFile(registerRequestDto.getSelfIntroVideo(), "videos"));
        }
        if (registerRequestDto.getProfilePicture() != null) {
            userDetail.setImageUrl(fileStorageService.storeFile(registerRequestDto.getProfilePicture(), "images"));
        }
        userDetailService.createUserDetail(userDetail);
    }

    @Override
    public ResponseEntity<CommonResponseDto<?>> login(LoginRequestDto loginRequestDto) {
        objectValidator.validate(loginRequestDto);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();

            String token = jwtUtil.generateToken(user);

            LoginResponseDto loginResponseDto = DtoMapper.loginResponseDto(user.getUsername(), user.getRole().name(), token);

            CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.OK, loginResponseDto);

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (AuthenticationException e) {
            log.error("Authentication exception", e);
            CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Incorrect email or password", HttpStatus.UNAUTHORIZED, "Login failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }
    }

    @Override
    public ResponseEntity<CommonResponseDto<?>> changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        objectValidator.validate(changePasswordRequestDto);

        User user = userService.getUserByUsername(changePasswordRequestDto.getUsername());

        if (passwordEncoder.matches(changePasswordRequestDto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getNewPassword()));
        } else {
            CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.OK, "Password does not match");
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }

        userService.updateUser(user);

        CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.OK, "Password changed successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Override
    public ResponseEntity<CommonResponseDto<?>> updateCustomer(String email, UserUpdateRequestDto userUpdateRequestDto) {
        objectValidator.validate(userUpdateRequestDto);

        UserDetail userDetail = userDetailService.getUserDetailByEmail(email);
        if (userDetail == null || userDetail.getUser().getRole() != EUserRole.CUSTOMER) {
            throw new RuntimeException("User not found");
        }
        User user = userDetail.getUser();

        if (userUpdateRequestDto.getFirstName() != null) {
            userDetail.setFirstName(userUpdateRequestDto.getFirstName());
        }
        if (userUpdateRequestDto.getMiddleName() != null) {
            userDetail.setMiddleName(userUpdateRequestDto.getMiddleName());
        }
        if (userUpdateRequestDto.getLastName() != null) {
            userDetail.setLastName(userUpdateRequestDto.getLastName());
        }
        if (userUpdateRequestDto.getEmail() != null) {
            userDetail.setEmail(userUpdateRequestDto.getEmail());
        }
        if (userUpdateRequestDto.getPhoneNumber() != null) {
            userDetail.setPhoneNumber(userUpdateRequestDto.getPhoneNumber());
        }
        if (userUpdateRequestDto.getAddress() != null) {
            userDetail.setAddress(userUpdateRequestDto.getAddress());
        }
        if (userUpdateRequestDto.getDateOfBirth() != null) {
            userDetail.setDateOfBirth(userUpdateRequestDto.getDateOfBirth());
        }
        if (userUpdateRequestDto.getNationalIdDocument() != null) {
            fileStorageService.deleteFile(userDetail.getDocumentUrl());
            userDetail.setDocumentUrl(fileStorageService.storeFile(userUpdateRequestDto.getNationalIdDocument(), "documents"));
        }
        if (userUpdateRequestDto.getSelfIntroVideo() != null) {
            fileStorageService.deleteFile(userDetail.getVideoUrl());
            userDetail.setVideoUrl(fileStorageService.storeFile(userUpdateRequestDto.getSelfIntroVideo(), "videos"));
        }
        if (userUpdateRequestDto.getProfilePicture() != null) {
            fileStorageService.deleteFile(userDetail.getImageUrl());
            userDetail.setImageUrl(fileStorageService.storeFile(userUpdateRequestDto.getProfilePicture(), "images"));
        }
        if (userUpdateRequestDto.getUsername() != null) {
            user.setUsername(userUpdateRequestDto.getUsername());
        }

        userService.updateUser(user);
        userDetail.setUser(user);
        userDetailService.updateUserDetail(userDetail);

        CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.OK, "Customer updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Override
    public ResponseEntity<CommonResponseDto<?>> updateAdmin(String email, UserUpdateRequestDto userUpdateRequestDto) {
        objectValidator.validate(userUpdateRequestDto);

        UserDetail userDetail = userDetailService.getUserDetailByEmail(email);
        if (userDetail == null || userDetail.getUser().getRole() != EUserRole.ADMIN) {
            throw new RuntimeException("User not found");
        }
        User user = userDetail.getUser();

        if (userUpdateRequestDto.getFirstName() != null) {
            userDetail.setFirstName(userUpdateRequestDto.getFirstName());
        }
        if (userUpdateRequestDto.getMiddleName() != null) {
            userDetail.setMiddleName(userUpdateRequestDto.getMiddleName());
        }
        if (userUpdateRequestDto.getLastName() != null) {
            userDetail.setLastName(userUpdateRequestDto.getLastName());
        }
        if (userUpdateRequestDto.getEmail() != null) {
            userDetail.setEmail(userUpdateRequestDto.getEmail());
        }
        if (userUpdateRequestDto.getPhoneNumber() != null) {
            userDetail.setPhoneNumber(userUpdateRequestDto.getPhoneNumber());
        }
        if (userUpdateRequestDto.getAddress() != null) {
            userDetail.setAddress(userUpdateRequestDto.getAddress());
        }
        if (userUpdateRequestDto.getDateOfBirth() != null) {
            userDetail.setDateOfBirth(userUpdateRequestDto.getDateOfBirth());
        }
        if (userUpdateRequestDto.getNationalIdDocument() != null) {
            fileStorageService.deleteFile(userDetail.getDocumentUrl());
            userDetail.setDocumentUrl(fileStorageService.storeFile(userUpdateRequestDto.getNationalIdDocument(), "documents"));
        }
        if (userUpdateRequestDto.getSelfIntroVideo() != null) {
            fileStorageService.deleteFile(userDetail.getVideoUrl());
            userDetail.setVideoUrl(fileStorageService.storeFile(userUpdateRequestDto.getSelfIntroVideo(), "videos"));
        }
        if (userUpdateRequestDto.getProfilePicture() != null) {
            fileStorageService.deleteFile(userDetail.getImageUrl());
            userDetail.setImageUrl(fileStorageService.storeFile(userUpdateRequestDto.getProfilePicture(), "images"));
        }
        if (userUpdateRequestDto.getUsername() != null) {
            user.setUsername(userUpdateRequestDto.getUsername());
        }

        userService.updateUser(user);
        userDetail.setUser(user);
        userDetailService.updateUserDetail(userDetail);

        CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Success", HttpStatus.OK, "Admin updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}

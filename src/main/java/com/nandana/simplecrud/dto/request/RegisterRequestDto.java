package com.nandana.simplecrud.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Register Request Data Transfer Object")
public class RegisterRequestDto {

    @Schema(description = "First name of the user", example = "John")
    @NotBlank(message = "first name cannot be blank")
    @Size(max = 50, message = "max first name is 50 characters")
    private String firstName;

    @Schema(description = "Middle name of the user (optional)", example = "Michael")
    @Size(max = 50, message = "max middle name is 50 characters")
    private String middleName;

    @Schema(description = "Last name of the user", example = "Doe")
    @NotBlank(message = "last name cannot be blank")
    @Size(max = 50, message = "max last name is 50 characters")
    private String lastName;

    @Schema(description = "Username for the user", example = "john_doe23")
    @NotBlank(message = "username cannot be blank")
    @Size(min = 6, max = 32, message = "username length min 6, max 32")
    private String username;

    @Schema(description = "Password for the user. Must contain at least one uppercase letter and one number", example = "Password1")
    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, max = 32, message = "password length min 8, max 32")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]*$",
            message = "Password must contain at least one uppercase letter and one number"
    )
    private String password;

    @Schema(description = "Email address of the user", example = "johndoe@example.com")
    @NotBlank(message = "email cannot be blank")
    @Email(message = "email is invalid format")
    private String email;

    @Schema(description = "Phone number of the user. Must start with +62 or 08 and contain 10 to 15 digits", example = "+628123456789")
    @NotBlank(message = "Phone number can't be blank")
    @Pattern(
            regexp = "^\\+(62)[ ]?[0-9]{9,13}$|^0[8][0-9]{9,12}$",
            message = "Phone number must be valid and contain 10 to 15 digits"
    )
    private String phoneNumber;

    @Schema(description = "Address of the user", example = "123 Main Street, Jakarta")
    @NotBlank(message = "address cannot be blank")
    private String address;

    @Schema(description = "Date of birth of the user. Must be in the past", example = "1990-01-01")
    @NotNull(message = "date of birth cannot be null")
    @Past(message = "date of Birth must be in the past")
    private Date dateOfBirth;

    @Schema(description = "Profile picture of the user", type = "string", format = "binary")
    private MultipartFile profilePicture;

    @Schema(description = "Self-introduction video of the user", type = "string", format = "binary")
    private MultipartFile selfIntroVideo;

    @Schema(description = "National ID document of the user", type = "string", format = "binary")
    private MultipartFile nationalIdDocument;
}

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
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Request DTO for updating user information")
public class UserUpdateRequestDto {

    @Size(max = 50, message = "max first name is 50 characters")
    @Schema(description = "The first name of the user", example = "John", maxLength = 50)
    private String firstName;

    @Size(max = 50, message = "max middle name is 50 characters")
    @Schema(description = "The middle name of the user", example = "M", maxLength = 50)
    private String middleName;

    @Size(max = 50, message = "max last name is 50 characters")
    @Schema(description = "The last name of the user", example = "Doe", maxLength = 50)
    private String lastName;

    @Size(min = 6, max = 32, message = "username length min 6, max 32")
    @Schema(description = "The username of the user", example = "john_doe", minLength = 6, maxLength = 32)
    private String username;

    @Email(message = "email is invalid format")
    @Schema(description = "The email address of the user", example = "john.doe@example.com")
    private String email;

    @Pattern(
            regexp = "^\\+(62)[ ]?[0-9]{9,13}$|^0[8][0-9]{9,12}$",
            message = "Phone number must be valid and contain 10 to 15 digits"
    )
    @Schema(description = "The phone number of the user", example = "+6281234567890", pattern = "^(\\+(62)[ ]?[0-9]{9,13}$|^0[8][0-9]{9,12}$)")
    private String phoneNumber;

    @Schema(description = "The address of the user", example = "123 Main St, Tasikmalaya, Indonesia")
    private String address;

    @Past(message = "date of Birth must be in the past")
    @Schema(description = "The date of birth of the user", example = "1990-05-15", type = "string", format = "date")
    private Date dateOfBirth;

    @Schema(description = "The profile picture of the user", type = "string", format = "binary")
    private MultipartFile profilePicture;

    @Schema(description = "The self-introduction video of the user", type = "string", format = "binary")
    private MultipartFile selfIntroVideo;

    @Schema(description = "The national ID document of the user", type = "string", format = "binary")
    private MultipartFile nationalIdDocument;
}

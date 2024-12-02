package com.nandana.simplecrud.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Request DTO for changing user password")
public class ChangePasswordRequestDto {

    @NotBlank(message = "username cannot be blank")
    @Size(min = 6, max = 32, message = "username length min 6, max 32")
    @Schema(description = "The username of the user whose password is being changed", example = "john_doe", minLength = 6, maxLength = 32)
    private String username;

    @NotBlank(message = "old password cannot be blank")
    @Size(min = 8, max = 32, message = "old password length min 8, max 32")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]*$",
            message = "Password must contain at least one uppercase letter and one number"
    )
    @Schema(description = "The current password of the user", example = "OldPassword123", minLength = 8, maxLength = 32)
    private String oldPassword;

    @NotBlank(message = "new password cannot be blank")
    @Size(min = 8, max = 32, message = "new password length min 8, max 32")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]*$",
            message = "Password must contain at least one uppercase letter and one number"
    )
    @Schema(description = "The new password for the user", example = "NewPassword123", minLength = 8, maxLength = 32)
    private String newPassword;
}

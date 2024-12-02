package com.nandana.simplecrud.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Component
public class ErrorResponseDto {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}

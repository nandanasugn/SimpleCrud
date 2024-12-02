package com.nandana.simplecrud.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Generic response structure for API responses")
public class CommonResponseDto<T> {
    @Schema(description = "Message describing the result of the operation", example = "Success")
    private String message;

    @Schema(description = "HTTP status code of the response", example = "200")
    private int status;

    @Schema(description = "The payload of the response. Can be any type of data", nullable = true)
    private T data;
}

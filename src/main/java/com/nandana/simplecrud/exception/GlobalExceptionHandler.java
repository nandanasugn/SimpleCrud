package com.nandana.simplecrud.exception;

import com.nandana.simplecrud.dto.response.CommonResponseDto;
import com.nandana.simplecrud.dto.response.ErrorResponseDto;
import com.nandana.simplecrud.mapper.DtoMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@ControllerAdvice
public class GlobalExceptionHandler {
    private String formatTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    private String extractErrorMessage(DataIntegrityViolationException e) {
        String message = e.getMostSpecificCause().getMessage();

        Pattern pattern = Pattern.compile("Key \\(([^)]+)\\)=\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String field = matcher.group(1);
            return capitalizeFirstLetter(field) + " already exists.";
        }

        return "Duplicate value exists.";
    }

    private String capitalizeFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException(Exception e, WebRequest request) {
        String customMessage = getCustomErrorMessage(e);

        ErrorResponseDto errorResponseDto = DtoMapper.errorResponseDto(formatTimeStamp(), HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", customMessage != null ? customMessage : e.getMessage(), request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponseDto<?>> handleValidationException(MethodArgumentNotValidException e, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        CommonResponseDto<?> responseDto = DtoMapper.commonResponseDto("Validation failed", HttpStatus.BAD_REQUEST, null);
        return ResponseEntity.badRequest().body(responseDto);
    }

    private String getCustomErrorMessage(Exception e) {
        if (e instanceof NullPointerException) {
            return "A required value was missing.";
        } else if (e instanceof IllegalArgumentException) {
            return "An invalid argument was provided.";
        } else if (e instanceof IllegalStateException) {
            return "The application is in an invalid state.";
        } else if (e instanceof IndexOutOfBoundsException) {
            return "An invalid index was accessed.";
        }

        return null;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException e, WebRequest request) {
        ErrorResponseDto errorResponseDto = DtoMapper.errorResponseDto(formatTimeStamp(), HttpStatus.CONFLICT, "Data Integrity Violation", extractErrorMessage(e), request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }
}

package com.example.exception;

import com.example.dto.responseDto.ErrorMsgResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

// Declare the class as a controller advice for handling exceptions
@RestControllerAdvice
public class AppExceptionHandler {

    // Handle validation errors caused by invalid method arguments
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(
                error -> {
                    errorMap.put(error.getField(), error.getDefaultMessage());
                }
        );
        return errorMap;
    }

    // Handle validation errors caused by constraint violations
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>("Validation failed: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Handle IllegalArgumentException by providing a custom error message
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>("Invalid input: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Handle custom UserException by creating a response entity with custom error details
    @ExceptionHandler(value = {UserException.class})
    ResponseEntity<ErrorMsgResponseDto> handleUserException(UserException ex, WebRequest webRequest) {
        return new ResponseEntity<>(
                new ErrorMsgResponseDto(false, ex.getStatus(), ex.getMsg()), HttpStatus.OK);
    }

    // Handle custom StudentException by creating a response entity with custom error details
    @ExceptionHandler(value = {StudentException.class})
    ResponseEntity<ErrorMsgResponseDto> handleStudentException(StudentException ex, WebRequest webRequest) {
        return new ResponseEntity<>(
                new ErrorMsgResponseDto(false, ex.getStatus(), ex.getMsg()), HttpStatus.OK);
    }

    // Handle custom PaymentSchemeException by creating a response entity with custom error details
    @ExceptionHandler(value = {PaymentSchemeException.class})
    ResponseEntity<ErrorMsgResponseDto> handlePaymentSchemeException(PaymentSchemeException ex, WebRequest webRequest) {
        return new ResponseEntity<>(
                new ErrorMsgResponseDto(false, ex.getStatus(), ex.getMsg()), HttpStatus.OK);
    }
}

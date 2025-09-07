package com.project.airBnb.advice;

import com.project.airBnb.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> resourceNotFound(ResourceNotFoundException e){
        ApiError error= ApiError.builder().status(HttpStatus.NOT_FOUND)
                .message(e.getMessage()).errorList(new ArrayList<>()).build();
        return buildApiResponse(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> internalServerError(Exception e){
        ApiError error=ApiError.builder().status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage()).errorList(new ArrayList<>()).build();
        return buildApiResponse(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> inputValidationError(MethodArgumentNotValidException e){
        List<String> errors=e.getBindingResult().getAllErrors()
                .stream().map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError error=ApiError.builder().status(HttpStatus.BAD_REQUEST)
                .message("Input Validation Failed").errorList(errors).build();
        return buildApiResponse(error);
    }

    private ResponseEntity<ApiResponse<?>> buildApiResponse(ApiError error){
        return new ResponseEntity<>(new ApiResponse<>(error), error.getStatus());
    }
}

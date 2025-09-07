package com.project.airBnb.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    private T data;
    private ApiError error;

    @JsonFormat(pattern = "HH:mm:ss  dd/MM/yyyy")
    private LocalDateTime time;

    public ApiResponse() {
        this.time=LocalDateTime.now();
    }

    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    public ApiResponse(ApiError error) {
        this();
        this.error = error;
    }
}

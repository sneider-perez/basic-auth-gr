package com.georincon.authserver.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class ErrorResponseDto {

    private Integer errorCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    private final List<String> messages;

    public ErrorResponseDto(Integer errorCode, String message) {
        this.messages = Collections.singletonList(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDto(Integer errorCode, List<String> messages) {
        this.messages = messages;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

}

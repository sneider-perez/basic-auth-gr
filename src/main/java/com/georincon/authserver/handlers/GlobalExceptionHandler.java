package com.georincon.authserver.handlers;

import com.georincon.authserver.dtos.ErrorResponseDto;
import com.georincon.authserver.exceptions.AuthException;
import com.georincon.authserver.exceptions.ResourceAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> notReadableException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto(10400, "Request con formato inválido"),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponseDto> authException(AuthException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto(10401, ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> authenticationException(AuthenticationException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto(11401, "Credenciales inválidas"),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> resourceAlreadyExists(ResourceAlreadyExistsException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto(10409, ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex) {
        log.error(ex.getMessage());

        return new ResponseEntity<>(
                new ErrorResponseDto(10500, "Ha ocurrido un error inesperado"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(String.format("El campo %s %s", error.getField(), error.getDefaultMessage()));
        }

        return new ResponseEntity<>(
                new ErrorResponseDto(11400, details),
                HttpStatus.BAD_REQUEST
        );
    }

}

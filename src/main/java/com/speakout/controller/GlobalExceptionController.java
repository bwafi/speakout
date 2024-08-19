package com.speakout.controller;

import com.speakout.DTOs.response.WebResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<WebResponse<String>> handleConstraintViolationException(ConstraintViolationException exception) {
    WebResponse<String> error = WebResponse.error(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
  
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<WebResponse<String>> handleBadCredentialsException(BadCredentialsException exception) {
    WebResponse<String> error = WebResponse.error(HttpStatus.UNAUTHORIZED.value(), "Username or password is invalid");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }
}

package com.speakout.controller;

import com.speakout.DTOs.request.RegisterRequest;
import com.speakout.DTOs.response.RegisterResponse;
import com.speakout.DTOs.response.WebResponse;
import com.speakout.constant.ApiUrl;
import com.speakout.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.AUTH_URL)
public class AuthController {
  
  @Autowired
  private AuthService authService;
  
  @PostMapping(path = "/register")
  public ResponseEntity<WebResponse<RegisterResponse>> register(@RequestBody RegisterRequest registerRequest) {
    RegisterResponse response = authService.register(registerRequest);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(WebResponse.success(HttpStatus.CREATED.value(), "Successfully registered", response));
  }
}

package com.speakout.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakout.DTOs.request.LoginRequest;
import com.speakout.DTOs.request.RegisterRequest;
import com.speakout.DTOs.response.LoginResponse;
import com.speakout.DTOs.response.RegisterResponse;
import com.speakout.DTOs.response.WebResponse;
import com.speakout.repository.RefreshTokenRepository;
import com.speakout.repository.UserRepository;
import com.speakout.service.AuthService;
import lombok.val;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
  @Autowired
  MockMvc mockMvc;
  
  @Autowired
  AuthService authService;
  
  @Autowired
  ObjectMapper objectMapper;
  
  @Autowired
  UserRepository userRepository;
  
  @Autowired
  RefreshTokenRepository refreshTokenRepository;
  
  @BeforeEach
  void setUp() {
    RegisterRequest registerRequest = RegisterRequest.builder()
        .firstName("Syahroni")
        .lastName("Bawafi")
        .username("wapi")
        .password("rahasia123")
        .build();
    
    authService.register(registerRequest);
  }
  
  @AfterEach
  void tearDown() {
    refreshTokenRepository.deleteAll();
    userRepository.deleteAll();
  }
  
  @Test
  void registerSuccessTest() throws Exception {
    RegisterRequest registerRequest = RegisterRequest.builder()
        .firstName("Syahroni")
        .lastName("Bawafi")
        .username("wapi1")
        .password("rahasia123")
        .build();
    
    mockMvc.perform(
        post("/api/v1/auth/register")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest))
    ).andExpect(
        status().isCreated()
    ).andExpectAll(
        jsonPath("$.statusCode").value(HttpStatus.CREATED.value()),
        jsonPath("$.message").value("Successfully registered user"),
        jsonPath("$.data.firstName").value("Syahroni"),
        jsonPath("$.data.lastName").value("Bawafi")
    );
  }
  
  
  @Test
  void registerFailedTest() throws Exception {
    RegisterRequest registerRequest = RegisterRequest.builder()
        .firstName("")
        .lastName("")
        .username("")
        .password("")
        .build();
    
    mockMvc.perform(
        post("/api/v1/auth/register")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest))
    ).andExpect(
        status().isBadRequest()
    ).andExpectAll(
        jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value())
    );
  }
  
  @Test
  void loginSuccessTest() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
        .username("wapi")
        .password("rahasia123")
        .build();
    
    mockMvc.perform(
        post("/api/v1/auth/login")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest))
    ).andExpect(
        status().isOk()
    ).andExpectAll(
        jsonPath("$.statusCode").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Successfully login")
    ).andDo(result -> {
      WebResponse<LoginResponse> loginResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<LoginResponse>>() {
      });
      Assertions.assertEquals(HttpStatus.OK.value(), loginResponse.getStatusCode());
      Assertions.assertEquals("Successfully login", loginResponse.getMessage());
    });
    
  }
  
  
  @Test
  void loginFailed_whenWrongPasswordTest() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
        .username("wapi")
        .password("wrong password")
        .build();
    
    mockMvc.perform(
        post("/api/v1/auth/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest))
    ).andExpect(
        status().isUnauthorized()
    ).andExpectAll(
        jsonPath("$.statusCode").value(HttpStatus.UNAUTHORIZED.value()),
        jsonPath("$.message").value("Username or password is invalid")
    );
    
  }
  
  
  @Test
  void loginFailed_whenValidationErrorTest() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
        .username("")
        .password("")
        .build();
    
    mockMvc.perform(
        post("/api/v1/auth/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest))
    ).andExpect(
        status().isBadRequest()
    ).andExpectAll(
        jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value())
    );
    
  }
  
}
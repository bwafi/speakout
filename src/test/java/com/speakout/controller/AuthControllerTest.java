package com.speakout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakout.DTOs.request.RegisterRequest;
import com.speakout.DTOs.response.RegisterResponse;
import com.speakout.repository.UserRepository;
import lombok.val;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
  ObjectMapper objectMapper;
  
  @Autowired
  UserRepository userRepository;
  
  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }
  
  @Test
  void registerSuccessTest() throws Exception {
    RegisterRequest registerRequest = RegisterRequest.builder()
        .firstName("Syahroni")
        .lastName("Bawafi")
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
        jsonPath("$.message").value("Successfully registered"),
        jsonPath("$.data.firstName").value("Syahroni"),
        jsonPath("$.data.lastName").value("Bawafi")
    );
  }
}
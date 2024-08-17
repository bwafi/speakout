package com.speakout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakout.DTOs.request.RegisterRequest;
import lombok.val;
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
  
  @Test
  void registerSuccessTest() throws Exception {
    RegisterRequest registerRequest = RegisterRequest.builder()
        .firstName("Syahroni")
        .lastName("bawafi")
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
        jsonPath("$.message").value("Successfully registered")
    );
  }
}
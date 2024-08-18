package com.speakout.DTOs.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
  
  @NotBlank
  @Size(min = 3, max = 100)
  private String username;
  
  @NotBlank
  @Size(min = 6, max = 100)
  private String password;
}


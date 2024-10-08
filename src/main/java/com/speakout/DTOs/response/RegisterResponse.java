package com.speakout.DTOs.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
  private String id;
  
  private String firstName;
  
  private String lastName;
  
  private String username;
}

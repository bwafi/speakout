package com.speakout.DTOs.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RegisterRequest {
  
  @NotBlank
  @Size(min = 3, max = 100)
  private String firstName;
  
  @NotBlank
  @Size(min = 3, max = 100)
  private String lastName;
  
  @NotBlank
  @Size(min = 6, max = 100)
  private String password;
}

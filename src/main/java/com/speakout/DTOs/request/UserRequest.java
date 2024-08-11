package com.speakout.DTOs.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.speakout.entity.ProfilePicture;
import com.speakout.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserRequest {
  
  @NotBlank
  @Size(min = 3, max = 100)
  private String username;
  
  @NotBlank
  @Size(min = 6, max = 100)
  private String password;
}

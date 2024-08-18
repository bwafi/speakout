package com.speakout.DTOs.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
  private String username;
  private String accessToken;
  private String refreshToken;
  private Long accessTokenExpiration;
  private Long refreshTokenExpiration;
  private String tokenType;
}

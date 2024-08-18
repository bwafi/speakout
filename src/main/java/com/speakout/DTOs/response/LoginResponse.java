package com.speakout.DTOs.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
  private String id;
  private String username;
  private String accessToken;
  private String refreshToken;
  private String accessTokenExpiration;
  private String refreshTokenExpiration;
  private String tokenType;
}

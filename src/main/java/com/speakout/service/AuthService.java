package com.speakout.service;

import com.speakout.DTOs.request.RegisterRequest;
import com.speakout.DTOs.response.RegisterResponse;

public interface AuthService {
  RegisterResponse register(RegisterRequest registerRequest);
}

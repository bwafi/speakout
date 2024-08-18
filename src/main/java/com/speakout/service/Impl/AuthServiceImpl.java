package com.speakout.service.Impl;

import com.speakout.DTOs.request.LoginRequest;
import com.speakout.DTOs.request.RegisterRequest;
import com.speakout.DTOs.response.LoginResponse;
import com.speakout.DTOs.response.RegisterResponse;
import com.speakout.entity.RefreshToken;
import com.speakout.entity.Role;
import com.speakout.entity.User;
import com.speakout.enums.ERoles;
import com.speakout.repository.RefreshTokenRepository;
import com.speakout.repository.RoleRepository;
import com.speakout.repository.UserRepository;
import com.speakout.security.JwtService;
import com.speakout.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
  
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private RoleRepository roleRepository;
  
  @Autowired
  private RefreshTokenRepository refreshTokenRepository;
  
  @Autowired
  private AuthenticationManager authenticationManager;
  
  @Autowired
  private ValidationService validationService;
  
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private JwtService jwtService;
  
  
  @Transactional
  @Override
  public RegisterResponse register(RegisterRequest registerRequest) {
    validationService.validate(registerRequest);
    
    Role roleUser = Role.builder()
        .name(ERoles.ROLE_USER)
        .build();
    
    Role roleCurrent = roleRepository.findById(1).orElseGet(() -> roleRepository.save(roleUser));
    
    User user = User.builder()
        .firstName(registerRequest.getFirstName())
        .lastName(registerRequest.getLastName())
        .username(registerRequest.getUsername())
        .password(passwordEncoder.encode(registerRequest.getPassword()))
        .role(roleCurrent)
        .build();
    
    User response = userRepository.save(user);
    
    return RegisterResponse.builder()
        .id(response.getId())
        .firstName(response.getFirstName())
        .lastName(response.getLastName())
        .username(response.getUsername())
        .build();
  }
  
  @Transactional
  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    validationService.validate(loginRequest);
    
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
    );
    
    SecurityContextHolder.getContext().setAuthentication(authenticate); // save Info User to Security Context
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
    
    String refreshToken = jwtService.generateRefreshToken(userDetails);
    String accessToken = jwtService.generateAccessToken(userDetails);
    
    saveRefreshToken(userDetails.getUsername(), refreshToken);
    
    Date expiresRefreshToken = jwtService.extractExpiration(refreshToken, false);
    Date expiresAccessToken = jwtService.extractExpiration(accessToken, true);
    
    return LoginResponse.builder()
        .username(userDetails.getUsername())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .accessTokenExpiration(expiresAccessToken.toInstant().toEpochMilli())
        .refreshTokenExpiration(expiresRefreshToken.toInstant().toEpochMilli())
        .tokenType("Bearer")
        .build();
    
  }
  
  protected void saveRefreshToken(String username, String token) {
    User userByUsername = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    
    Date expiresRefreshToken = jwtService.extractExpiration(token, false);
    
    RefreshToken refreshToken = RefreshToken.builder()
        .user(userByUsername)
        .refreshToken(token)
        .expiresIn(expiresRefreshToken.toInstant().toEpochMilli())
        .build();
    
    refreshTokenRepository.save(refreshToken);
  }
  
  
}

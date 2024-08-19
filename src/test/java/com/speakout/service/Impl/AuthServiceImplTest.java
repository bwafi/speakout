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
import com.speakout.security.jwt.JwtService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
  
  @Mock
  private UserRepository userRepository;
  
  @Mock
  private RoleRepository roleRepository;
  
  @Mock
  private PasswordEncoder passwordEncoder;
  
  @Mock
  private AuthenticationManager authenticationManager;
  
  @Mock
  private ValidationService validationService;
  
  @Mock
  JwtService jwtService;
  
  @Mock
  RefreshTokenRepository refreshTokenRepository;
  
  @InjectMocks
  private AuthServiceImpl authService;
  
  @BeforeEach
  void setUp() {
//    RegisterRequest registerRequest = RegisterRequest.builder()
//        .firstName("Syahroni")
//        .lastName("Bawafi")
//        .username("wapi")
//        .password("rahasia")
//        .build();
//
//    authService.register(registerRequest);
  }
  
  @Test
  void registerTest() {
    Role roleUser = Role.builder()
        .name(ERoles.ROLE_USER)
        .build();
    
    RegisterRequest registerRequest = RegisterRequest.builder()
        .firstName("Syahroni")
        .lastName("Bawafi")
        .username("wapi2")
        .password("rahasia")
        .build();
    
    User user = User.builder()
        .firstName("Syahroni")
        .lastName("Bawafi")
        .password("rahasia")
        .role(roleUser)
        .build();
    
    RegisterResponse expectedResponse = RegisterResponse.builder()
        .firstName("Syahroni")
        .lastName("Bawafi")
        .username("S.Bawafi323")
        .build();
    
    when(roleRepository.findById(1)).thenReturn(Optional.of(roleUser));
    when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
    when(passwordEncoder.encode("rahasia")).thenReturn("PasswordEncode");
    
    RegisterResponse response = authService.register(registerRequest);
    
    assertNotNull(response);
    assertEquals(expectedResponse.getFirstName(), response.getFirstName());
    assertEquals(expectedResponse.getLastName(), response.getLastName());
    
    verify(roleRepository, Mockito.times(1)).findById(1);
    verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    verify(roleRepository, Mockito.times(0)).save(new Role());
    verify(passwordEncoder, Mockito.times(1)).encode("rahasia");
  }
  
  
  @Test
  void registerCreateRole_WhenRoleNullTest() {
    Role roleUser = Role.builder()
        .name(ERoles.ROLE_USER)
        .build();
    
    RegisterRequest registerRequest = RegisterRequest.builder()
        .firstName("Syahroni")
        .username("wapi")
        .lastName("Bawafi")
        .password("rahasia")
        .build();
    
    User user = User.builder()
        .firstName("Syahroni")
        .lastName("Bawafi")
        .username("wapi")
        .password("rahasia")
        .role(roleUser)
        .build();
    
    RegisterResponse expectedResponse = RegisterResponse.builder()
        .firstName("Syahroni")
        .lastName("Bawafi")
        .username("S.Bawafi323")
        .build();
    
    when(roleRepository.findById(1)).thenReturn(Optional.empty());
    when(roleRepository.save(any(Role.class))).thenReturn(roleUser);
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(passwordEncoder.encode("rahasia")).thenReturn("PasswordEncode");
    
    RegisterResponse response = authService.register(registerRequest);
    
    assertNotNull(response);
    assertEquals(expectedResponse.getFirstName(), response.getFirstName());
    assertEquals(expectedResponse.getLastName(), response.getLastName());
    
    verify(roleRepository, Mockito.times(1)).findById(1);
    verify(userRepository, Mockito.times(1)).save(any(User.class));
    verify(roleRepository, times(1)).save(any(Role.class));
    verify(passwordEncoder, times(1)).encode("rahasia");
  }
  
  
  @Test
  void registerWhenValidateFailedTest() {
    RegisterRequest registerRequest = RegisterRequest.builder()
        .firstName("")
        .lastName("")
        .password("")
        .build();
    
    doThrow(ConstraintViolationException.class)
        .when(validationService)
        .validate(registerRequest);
    
    assertThrows(ConstraintViolationException.class, () -> {
      authService.register(registerRequest);
    });
    
    verify(roleRepository, never()).findById(1);
    verify(userRepository, never()).save(any(User.class));
    verify(roleRepository, never()).save(any(Role.class));
    
  }
  
  @Test
  void loginSuccessTest() {
    LoginRequest loginRequest = LoginRequest.builder()
        .username("syahroni")
        .password("rahasia123")
        .build();
    
    Authentication authentication = mock(Authentication.class);
    UserDetailsImpl userDetails = UserDetailsImpl.builder()
        .username("syahroni")
        .password("rahasia123")
        .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
        .build();
    
    User mockUser = User.builder()
        .id(UUID.randomUUID().toString())
        .username("syahroni")
        .password("encodedPassword")
        .build();
    
    
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(mockUser));
    when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(new RefreshToken());
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(jwtService.generateAccessToken(userDetails)).thenReturn("MockAccessToken");
    when(jwtService.generateRefreshToken(userDetails)).thenReturn("MockRefreshToken");
    when(jwtService.extractExpiration(any(String.class), eq(false))).thenReturn(new Date());
    when(jwtService.extractExpiration(any(String.class), eq(true))).thenReturn(new Date());
    
    LoginResponse login = authService.login(loginRequest);
    
    assertNotNull(login);
    assertEquals("syahroni", login.getUsername());
    
    verify(validationService, times(1)).validate(any(LoginRequest.class));
    verify(authentication, times(1)).getPrincipal();
    verify(jwtService, times(1)).generateAccessToken(userDetails);
    verify(jwtService, times(1)).generateRefreshToken(userDetails);
    verify(jwtService, times(2)).extractExpiration("MockRefreshToken", false);
    verify(jwtService, times(1)).extractExpiration("MockAccessToken", true);
    verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    
  }
  
  
  @Test
  void loginFailed_whenUserNotFoundTest() {
    LoginRequest loginRequest = LoginRequest.builder()
        .username("syahroni")
        .password("rahasia123")
        .build();
    
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new UsernameNotFoundException("Username not found"));
    
    UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, () -> authService.login(loginRequest));
    assertNotNull(usernameNotFoundException);
    assertEquals("Username not found", usernameNotFoundException.getMessage());
    
    verify(validationService, times(1)).validate(any(LoginRequest.class));
    verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
  }
  
  
  @Test
  void loginFailed_whenInvalidPasswordTest() {
    LoginRequest loginRequest = LoginRequest.builder()
        .username("syahroni")
        .password("wrongpassword")
        .build();
    
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Invalid password"));
    
    BadCredentialsException thrownException = assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
    
    assertNotNull(thrownException);
    assertEquals("Invalid password", thrownException.getMessage());
    
    verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
  }
  
  @Test
  void loginFailed_whenValidationErrorTest() {
    LoginRequest loginRequest = LoginRequest.builder()
        .username("")
        .password("")
        .build();
    
    doThrow(ConstraintViolationException.class).when(validationService).validate(loginRequest);
    
    assertThrows(ConstraintViolationException.class, () -> {
      authService.login(loginRequest);
    });
    
    verify(validationService, times(1)).validate(loginRequest);
  }
  
}

package com.speakout.service.Impl;

import com.speakout.DTOs.request.RegisterRequest;
import com.speakout.DTOs.response.RegisterResponse;
import com.speakout.entity.Role;
import com.speakout.entity.User;
import com.speakout.enums.ERoles;
import com.speakout.repository.RoleRepository;
import com.speakout.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
  
  @Mock
  private UserRepository userRepository;
  
  @Mock
  private RoleRepository roleRepository;
  
  @InjectMocks
  private AuthServiceImpl authService;
  
  @Test
  void registerTest() {
    Role roleUser = Role.builder()
        .name(ERoles.ROLE_USER)
        .build();
    
    RegisterRequest registerRequest = RegisterRequest.builder()
        .firstName("Syahroni")
        .lastName("Bawafi")
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
    
    Mockito.when(roleRepository.findById(1)).thenReturn(Optional.of(roleUser));
    Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
    
    RegisterResponse response = authService.register(registerRequest);
    
    assertNotNull(response);
    assertEquals(expectedResponse.getFirstName(), response.getFirstName());
    assertEquals(expectedResponse.getLastName(), response.getLastName());
    
    Mockito.verify(roleRepository, Mockito.times(1)).findById(1);
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
  }
}

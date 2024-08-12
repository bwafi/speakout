package com.speakout.service.Impl;

import com.speakout.DTOs.request.RegisterRequest;
import com.speakout.DTOs.response.RegisterResponse;
import com.speakout.entity.Role;
import com.speakout.entity.User;
import com.speakout.enums.ERoles;
import com.speakout.repository.RoleRepository;
import com.speakout.repository.UserRepository;
import com.speakout.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
  
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private RoleRepository roleRepository;
  
  @Autowired
  private ValidationService validationService;
  
  @Override
  public RegisterResponse register(RegisterRequest registerRequest) {
    validationService.validate(registerRequest);
    
    Role roleCurrent = roleRepository.findById(1).orElse(null);
    
    Role roleUser = Role.builder()
        .name(ERoles.ROLE_USER)
        .build();
    
    if (roleCurrent == null) {
      roleCurrent = roleRepository.save(roleUser);
    }
    
    User user = User.builder()
        .firstName(registerRequest.getFirstName())
        .lastName(registerRequest.getLastName())
        .password(registerRequest.getPassword())
        .role(roleCurrent)
        .build();
    
    User response = userRepository.save(user);
    
    return RegisterResponse.builder()
        .firstName(response.getFirstName())
        .lastName(response.getLastName())
        .username(response.getUsername())
        .build();
  }
}

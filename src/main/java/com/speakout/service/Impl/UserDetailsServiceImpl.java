package com.speakout.service.Impl;

import com.speakout.entity.User;
import com.speakout.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  
  @Autowired
  private UserRepository userRepository;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User userByUsername = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not Found"));
    
    return UserDetailsImpl.builder()
        .username(userByUsername.getUsername())
        .password(userByUsername.getPassword())
        .authorities(List.of(new SimpleGrantedAuthority(userByUsername.getRole().toString()))) // add role o UserDetail with SimpleGrantedAuthority
        .build();
  }
}

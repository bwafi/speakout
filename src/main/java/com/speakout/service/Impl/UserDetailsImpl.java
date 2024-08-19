package com.speakout.service.Impl;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
  private String id;
  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;
  
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }
  @Override
  public String getPassword() {
    return this.password;
  }
  
  @Override
  public String getUsername() {
    return this.username;
  }
  
  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }
  
  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }
  
  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }
  
  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}

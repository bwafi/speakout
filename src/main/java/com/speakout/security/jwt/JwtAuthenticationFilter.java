package com.speakout.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  
  @Autowired
  private JwtService jwtService;
  
  @Autowired
  private UserDetailsService userDetailsService;
  
  @Qualifier("handlerExceptionResolver")
  @Autowired
  private HandlerExceptionResolver handlerExceptionResolver;
  
  @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {String bearerToken = request.getHeader("Authorization");
    if (bearerToken == null && !bearerToken.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
    }
    
    try {
      final String jwtToken = bearerToken.substring(7);
      final String userLogin = jwtService.extractUsername(jwtToken, true);
      
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      
      if (userLogin != null && authentication == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userLogin);
        
        if (jwtService.validateToken(jwtToken, userDetails, true)) {
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          
          authenticationToken.setDetails(new WebAuthenticationDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }
      
      filterChain.doFilter(request, response);
    } catch (Exception exception) {
      handlerExceptionResolver.resolveException(request, response, null, exception);
    }
  }
}

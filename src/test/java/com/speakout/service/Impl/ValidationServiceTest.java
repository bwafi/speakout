package com.speakout.service.Impl;

import com.speakout.DTOs.request.RegisterRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {
  
  @Mock
  private Validator validator;
  
  @InjectMocks
  private ValidationService validationService;
  
  @Test
  void testValidateRegisterRequestWithViolations() {
    RegisterRequest request = RegisterRequest.builder()
        .firstName("Jo")
        .lastName("Doe")
        .password("123456")
        .build();
    
    ConstraintViolation<RegisterRequest> violation = mock(ConstraintViolation.class);
    Set<ConstraintViolation<RegisterRequest>> violations = Set.of(violation);
    when(validator.validate(request)).thenReturn(violations);
    when(violation.getMessage()).thenReturn("First name must be at least 3 characters");
    
    ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
      validationService.validate(request);
    });
    
    assertTrue(exception.getConstraintViolations().contains(violation));
  }
  
  @Test
  void testValidateRegisterRequestWithNoViolations() {
    RegisterRequest request = RegisterRequest.builder()
        .firstName("Syahroni")
        .lastName("Bawafi")
        .password("rahasia123")
        .build();
    
    when(validator.validate(request)).thenReturn(Set.of());
    
    assertDoesNotThrow(() -> validationService.validate(request));
  }
}

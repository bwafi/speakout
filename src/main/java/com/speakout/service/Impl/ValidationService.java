package com.speakout.service.Impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationService {
   @Autowired
  private Validator validator;
   
   public void validate(Object object) {
     Set<ConstraintViolation<Object>> validate = validator.validate(object);
     if(!validate.isEmpty()) {
       throw new ConstraintViolationException(validate);
     }
   }
  
}

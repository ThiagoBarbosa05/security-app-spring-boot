package com.example.security.validation.validators;

import com.example.security.user.dto.RegisterUserRequest;
import com.example.security.user.dto.UpdateUserPasswordRequest;
import com.example.security.user.dto.UpdateUserRequest;
import com.example.security.validation.annotations.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {}

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {

    try {
      Method getPasswordMethod = obj.getClass().getMethod("getPassword");
      Method getMatchingPasswordMethod  = obj.getClass().getMethod("getMatchingPassword");

      String password = (String) getPasswordMethod.invoke(obj);
      String matchingPassword = (String) getMatchingPasswordMethod.invoke(obj);

      return password != null && password.equals(matchingPassword);
    } catch (NoSuchMethodException | IllegalAccessException |
             InvocationTargetException e) {
      throw new RuntimeException(e);
    }

  }

}


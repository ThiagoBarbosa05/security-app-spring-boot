package com.example.security.validation.annotations;

import com.example.security.validation.validators.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {
  String message() default "Invalid email";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}

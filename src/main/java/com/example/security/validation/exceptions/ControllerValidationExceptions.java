package com.example.security.validation.exceptions;

import com.example.security.dto.ExceptionMessageResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerValidationExceptions {
  private final MessageSource messageSource;

  public ControllerValidationExceptions(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ExceptionMessageResponse>> handler(
    MethodArgumentNotValidException methodException
  ) {
    List<ExceptionMessageResponse> messageResponseList = new ArrayList<>();

    methodException.getBindingResult().getFieldErrors().forEach(exception -> {
      String message = messageSource.getMessage(exception, LocaleContextHolder.getLocale());

      ExceptionMessageResponse messageResponse = new ExceptionMessageResponse(
        message,
        HttpStatus.BAD_REQUEST.value(),
        exception.getField(),
        LocalDateTime.now()
      );

      messageResponseList.add(messageResponse);
    });

    methodException.getBindingResult().getGlobalErrors().forEach(exception -> {
      String message = messageSource.getMessage(exception, LocaleContextHolder.getLocale());

      ExceptionMessageResponse messageResponse = new ExceptionMessageResponse(
        message,
        HttpStatus.BAD_REQUEST.value(),
        exception.getObjectName(),
        LocalDateTime.now()
      );

      messageResponseList.add(messageResponse);
    });

    return new ResponseEntity<>(messageResponseList, HttpStatus.BAD_REQUEST);
  }
}

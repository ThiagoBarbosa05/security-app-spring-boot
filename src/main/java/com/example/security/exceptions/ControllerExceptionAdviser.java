package com.example.security.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.security.dto.ExceptionMessageResponse;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionAdviser {
  private final MessageSource messageSource;

  public ControllerExceptionAdviser(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
    ExceptionMessageResponse responseErrorDTO = new ExceptionMessageResponse(
      exception.getMessage(),
      HttpStatus.CONFLICT.value(),
      null,
      LocalDateTime.now()
    );

    return new ResponseEntity<>(responseErrorDTO, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException exception) {
    ExceptionMessageResponse responseErrorDTO = new ExceptionMessageResponse(
      exception.getMessage(),
      HttpStatus.UNAUTHORIZED.value(),
      null,
      LocalDateTime.now()
    );

    return new ResponseEntity<>(responseErrorDTO, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(EmptyFileException.class)
  public ResponseEntity<Object> handleEmptyFileException(EmptyFileException exception) {
    ExceptionMessageResponse responseErrorDTO = new ExceptionMessageResponse(
      exception.getMessage(),
      HttpStatus.BAD_REQUEST.value(),
      null,
      LocalDateTime.now()
    );

    return new ResponseEntity<>(responseErrorDTO, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidFileTypeException.class)
  public ResponseEntity<Object> handleInvalidFileTypeException(InvalidFileTypeException exception) {
    ExceptionMessageResponse responseErrorDTO = new ExceptionMessageResponse(
      exception.getMessage(),
      HttpStatus.BAD_REQUEST.value(),
      null,
      LocalDateTime.now()
    );

    return new ResponseEntity<>(responseErrorDTO, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
    ExceptionMessageResponse responseErrorDTO = new ExceptionMessageResponse(
      exception.getMessage(),
      HttpStatus.BAD_REQUEST.value(),
      null,
      LocalDateTime.now()
    );

    return new ResponseEntity<>(responseErrorDTO, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResetPasswordTokenExpiredException.class)
  public ResponseEntity<Object> handleResetPasswordTokenExpiredException(ResetPasswordTokenExpiredException exception) {
    ExceptionMessageResponse responseErrorDTO = new ExceptionMessageResponse(
      exception.getMessage(),
      HttpStatus.BAD_REQUEST.value(),
      null,
      LocalDateTime.now()
    );

    return new ResponseEntity<>(responseErrorDTO, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TokenNotFoundException.class)
  public ResponseEntity<Object> handleTokenNotFoundException(TokenNotFoundException exception) {
    ExceptionMessageResponse responseErrorDTO = new ExceptionMessageResponse(
      exception.getMessage(),
      HttpStatus.NOT_FOUND.value(),
      null,
      LocalDateTime.now()
    );

    return new ResponseEntity<>(responseErrorDTO, HttpStatus.NOT_FOUND);
  }

}

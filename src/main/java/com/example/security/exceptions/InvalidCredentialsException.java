package com.example.security.exceptions;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Credenciais inválidas");
  }
}

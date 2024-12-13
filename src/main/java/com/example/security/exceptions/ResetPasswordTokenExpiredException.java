package com.example.security.exceptions;

public class ResetPasswordTokenExpiredException extends RuntimeException {
  public ResetPasswordTokenExpiredException() {
    super("Token para redefinição de senha expirado");
  }
}

package com.example.security.exceptions;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("Usuário não encontrado ou inexistente");
  }
}

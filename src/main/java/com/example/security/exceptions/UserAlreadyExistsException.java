package com.example.security.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
  public UserAlreadyExistsException() {
    super("Usuário já existe");
  }
}

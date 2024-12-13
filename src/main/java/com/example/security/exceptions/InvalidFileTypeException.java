package com.example.security.exceptions;

public class InvalidFileTypeException extends RuntimeException {
  public InvalidFileTypeException() {
    super("Tipo de arquivo inválido");
  }
}

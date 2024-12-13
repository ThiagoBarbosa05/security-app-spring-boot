package com.example.security.exceptions;

public class EmptyFileException extends RuntimeException {
  public EmptyFileException() {
    super("Arquivo está vazio");
  }
}

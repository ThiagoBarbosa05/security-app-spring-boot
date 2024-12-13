package com.example.security.dto;

import java.time.LocalDateTime;

public record ExceptionMessageResponse(
  String message,
  Integer statusCode,
  String field,
  LocalDateTime time
) {

}

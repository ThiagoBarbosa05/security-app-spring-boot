package com.example.security.user.dto;

import com.example.security.validation.annotations.ValidEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


public record LoginUserRequest(

  @NotNull
  @NotEmpty
  @ValidEmail
  String email,

  @NotEmpty
  @NotNull
  String password
) {
}

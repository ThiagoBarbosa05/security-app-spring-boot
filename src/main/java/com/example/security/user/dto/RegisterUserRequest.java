package com.example.security.user.dto;

import com.example.security.validation.annotations.PasswordMatches;
import com.example.security.validation.annotations.ValidEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@PasswordMatches
public class RegisterUserRequest {
  @NotNull
  @NotEmpty
  private String firstName;

  @NotEmpty
  @NotNull
  private String lastName;

  @NotEmpty
  @NotNull
  private String password;
  private String matchingPassword;

  @ValidEmail
  @NotEmpty
  @NotNull
  private String email;

}

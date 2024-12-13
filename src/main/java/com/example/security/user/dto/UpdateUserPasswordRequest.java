package com.example.security.user.dto;

import com.example.security.validation.annotations.PasswordMatches;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@PasswordMatches
@Data
public class UpdateUserPasswordRequest {

  private UUID token;

  @NotEmpty
  @NotNull
  private String password;
  private String matchingPassword;

}

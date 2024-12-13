package com.example.security.user.dto;

import com.example.security.validation.annotations.ValidEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
    @NotNull
    @NotEmpty
    String firstName,

    @NotEmpty
    @NotNull
    String lastName
) {}

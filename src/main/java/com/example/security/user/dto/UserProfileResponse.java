package com.example.security.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserProfileResponse(
  UUID id,
  String firstName,
  String lastName,
  String email,
  String avatar,
  LocalDateTime createdAt
) {

}

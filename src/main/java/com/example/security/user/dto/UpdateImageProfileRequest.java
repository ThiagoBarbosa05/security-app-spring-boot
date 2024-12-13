package com.example.security.user.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UpdateImageProfileRequest(
  String key,
  UUID userId
) {

}

package com.example.security.providers;

import com.example.security.providers.dto.JwtPayload;

public interface JwtProvider {
  String generateToken(JwtPayload jwtPayload);
  String getSubjectFromToken(String token);
  boolean isTokenValid(String token);
}

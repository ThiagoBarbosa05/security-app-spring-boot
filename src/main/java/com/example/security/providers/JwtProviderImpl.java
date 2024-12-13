package com.example.security.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.security.providers.dto.JwtPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class JwtProviderImpl implements JwtProvider {


  private final String secretKey ;
  private static final String ISSUER = "security-app";
  private final Algorithm algorithm;
  private final JWTVerifier verifier;

  public JwtProviderImpl(@Value("${security.token.secret}") String secretKey) {
    this.secretKey = secretKey;
    this.algorithm = Algorithm.HMAC256(secretKey);
    this.verifier = JWT.require(algorithm).build();
  }

  public String generateToken(JwtPayload jwtPayload) {
    try {
      return JWT.create()
        .withIssuer(ISSUER)
        .withIssuedAt(creationDate())
        .withExpiresAt(expirationDate())
        .withSubject(jwtPayload.subject())
        .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new JWTCreationException("Erro ao gerar token", exception);
    }
  }

  public String getSubjectFromToken(String token) {
    try {
      return JWT.require(algorithm)
        .withIssuer(ISSUER)
        .build()
        .verify(token)
        .getSubject();
    } catch (JWTVerificationException exception) {
      throw new JWTVerificationException("Token inválido ou expirado");
    }
  }

  public boolean isTokenValid(String token) {
    try {
      verifier.verify(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private Instant creationDate() {
    return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();
  }

  private Instant expirationDate() {
    return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusHours(2).toInstant();
  }
}

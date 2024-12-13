package com.example.security.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Enumerated(EnumType.STRING)
  private TokenType type;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
  }
}

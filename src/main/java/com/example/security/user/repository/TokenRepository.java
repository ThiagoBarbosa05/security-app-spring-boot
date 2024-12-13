package com.example.security.user.repository;


import com.example.security.user.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    void deleteAllByUserId(UUID userId);
}

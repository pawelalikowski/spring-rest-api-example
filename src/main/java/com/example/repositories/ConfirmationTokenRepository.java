package com.example.repositories;

import com.example.dictionaries.TokenType;
import com.example.models.ConfirmationToken;
import com.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByUserAndToken(User user, String token);

    Optional<ConfirmationToken> findByUserAndTokenAndTokenType(User user, String token, TokenType tokenType);
}

package com.example.repositories;

import com.example.dictionaries.TokenStatus;
import com.example.models.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenStatusRepository extends JpaRepository<TokenStatus, Long> {
}

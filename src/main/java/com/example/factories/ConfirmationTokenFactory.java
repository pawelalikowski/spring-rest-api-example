package com.example.factories;

import com.example.dictionaries.TokenType;
import com.example.models.ConfirmationToken;
import com.example.models.User;
import com.example.util.TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ConfirmationTokenFactory {

    private final TokenGenerator tokenGenerator;

    public ConfirmationTokenFactory(TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    public ConfirmationToken create(User user, TokenType tokenType) {
        return new ConfirmationToken(tokenGenerator.generateToken(), user, tokenType, new Date());
    }
}

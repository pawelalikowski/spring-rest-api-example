package com.example.services;

import com.example.dictionaries.TokenStatus;
import com.example.dictionaries.TokenType;
import com.example.exceptions.RestError;
import com.example.exceptions.RestException;
import com.example.factories.ConfirmationTokenFactory;
import com.example.factories.MailMessageFactory;
import com.example.models.ConfirmationToken;
import com.example.models.PasswordResetRequest;
import com.example.models.User;
import com.example.repositories.ConfirmationTokenRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthService {

    private final MailService mailService;
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository tokenRepository;
    private final ConfirmationTokenFactory tokenFactory;
    private final MailMessageFactory mailMessageFactory;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AuthService(UserRepository userRepository, MailService mailService,
                       ConfirmationTokenRepository tokenRepository,
                       ConfirmationTokenFactory tokenFactory,
                       MailMessageFactory mailMessageFactory,
                       BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
        this.tokenFactory = tokenFactory;
        this.mailMessageFactory = mailMessageFactory;
        this.encoder = encoder;
    }


    @Transactional
    public void register(final User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(user1 -> {
            throw new RestException(RestError.EmailTaken, user1.getEmail() + " address already exists in our database.");
        });
        ConfirmationToken token = tokenFactory.create(user, TokenType.EMAIL_CONFIRMATION);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        tokenRepository.save(token);
        mailService.sendMail(mailMessageFactory.create()
                .from("API")
                .to(user.getEmail())
                .subject("Confirm registration")
                .template("registration-confirmation.ftl")
                .addModel("user", user)
                .addModel("token", token)
                .build());
    }

    @Transactional
    public void confirm(String email, String token) {
        verifyToken(token, email, TokenType.EMAIL_CONFIRMATION);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new RestException(RestError.NonExistentUser)
        );
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new RestException(RestError.NonExistentUser)
        );
        ConfirmationToken token = tokenFactory.create(user, TokenType.PASSWORD_RESET);
        tokenRepository.save(token);
        mailService.sendMail(mailMessageFactory.create()
                .from("API")
                .to(user.getEmail())
                .template("password-reset.ftl")
                .subject("Confirm registration")
                .addModel("user", user)
                .addModel("token", token)
                .build());
    }

    @Transactional
    public void passwordReset(PasswordResetRequest passwordResetRequest) {
        verifyToken(passwordResetRequest.getToken(), passwordResetRequest.getEmail(), TokenType.PASSWORD_RESET);
        User user = userRepository.findByEmail(passwordResetRequest.getEmail()).orElseThrow(() ->
                new RestException(RestError.NonExistentUser)
        );
        user.setPassword(encoder.encode(passwordResetRequest.getPassword()));
        userRepository.save(user);
    }

    private void verifyToken(String token, String email, TokenType tokenType) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new RestException(RestError.EmailNotFound, "Email not found: " + email)
        );
        ConfirmationToken confirmationToken = tokenRepository.findByUserAndTokenAndTokenType(user, token, tokenType).orElseThrow(() -> new IllegalArgumentException("Bad token"));
        if ( ! TokenStatus.PENDING.equals(confirmationToken.getTokenStatus()))
            throw new RestException(RestError.IllegalTokenStatus, "Token is " + confirmationToken.getTokenStatus().getValue());
        confirmationToken.setTokenStatus(TokenStatus.COMPLETED);
        tokenRepository.save(confirmationToken);
    }
}

package com.example.services;

import com.example.dictionaries.TokenStatus;
import com.example.dictionaries.TokenType;
import com.example.factories.ConfirmationTokenFactory;
import com.example.factories.MailMessageFactory;
import com.example.models.ConfirmationToken;
import com.example.models.PasswordResetRequest;
import com.example.models.User;
import com.example.repositories.ConfirmationTokenRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthService {

    private final MailService mailService;
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository tokenRepository;
    private final ConfirmationTokenFactory tokenFactory;
    private final MailMessageFactory mailMessageFactory;

    @Autowired
    public AuthService(UserRepository userRepository, MailService mailService,
                       ConfirmationTokenRepository tokenRepository,
                       ConfirmationTokenFactory tokenFactory, MailMessageFactory mailMessageFactory) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
        this.tokenFactory = tokenFactory;
        this.mailMessageFactory = mailMessageFactory;
    }


    @Transactional
    public void register(final User user) {
        ConfirmationToken token = tokenFactory.create(user, TokenType.EMAIL_CONFIRMATION);
        String text = "Somebody registered your email on our site. If you wish to confirm registration please click on link below:"
                + "http://localhost:8080/auth/confirm?user=" + user.getEmail() + "&token=" + token.getToken();
        String subject = "Confirm registration";
        userRepository.save(user);
        tokenRepository.save(token);
        mailService.sendMail(mailMessageFactory.create().to(user.getEmail()).from("API").text(text).subject(subject).build());
    }

    @Transactional
    public void confirm(String email, String token) {
        verifyToken(token, email, TokenType.EMAIL_CONFIRMATION);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Bad username"));
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User does not exists"));
        ConfirmationToken token = tokenFactory.create(user, TokenType.PASSWORD_RESET);
        String text = "Somebody requested password reset for your account. If you wish to reset password please click link below:"
                + "http://localhost:8080/resetPassword?user=" + user.getEmail() + "&token=" + token.getToken();
        String subject = "Confirm registration";
        tokenRepository.save(token);
        mailService.sendMail(mailMessageFactory.create().to(user.getEmail()).from("API").text(text).subject(subject).build());
    }

    @Transactional
    public void passwordReset(PasswordResetRequest passwordResetRequest) {
        verifyToken(passwordResetRequest.getToken(), passwordResetRequest.getEmail(), TokenType.PASSWORD_RESET);
        User user = userRepository.findByEmail(passwordResetRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("User does not exists"));
        user.setPassword(passwordResetRequest.getPassword());
        userRepository.save(user);
    }

    private void verifyToken(String token, String email, TokenType tokenType) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User does not exits"));
        ConfirmationToken confirmationToken = tokenRepository.findByUserAndTokenAndTokenType(user, token, tokenType).orElseThrow(() -> new IllegalArgumentException("Bad token"));
        if ( ! confirmationToken.getTokenStatus().equals(TokenStatus.PENDING)) throw new IllegalStateException("Bad token status");
        confirmationToken.setTokenStatus(TokenStatus.COMPLETED);
        tokenRepository.save(confirmationToken);
    }
}

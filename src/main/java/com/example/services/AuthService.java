package com.example.services;

import com.example.dictionaries.TokenStatus;
import com.example.models.ConfirmationToken;
import com.example.models.User;
import com.example.repositories.ConfirmationTokenRepository;
import com.example.repositories.UserRepository;
import com.example.util.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Optional;

@Service
public class AuthService {

    private final MailService mailService;
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository tokenRepository;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public AuthService(UserRepository userRepository, MailService mailService, ConfirmationTokenRepository tokenRepository, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
        this.tokenGenerator = tokenGenerator;
    }


    @Transactional
    public void register(final User user) {
        String token = tokenGenerator.generateToken();
        String text = "Somebody registered your email on our site. If you wish to confirm registration please click on link below:"
                + "http://localhost:8080/auth/confirm?user=" + user.getEmail() + "&token=" + token;
        String subject = "Confirm registration";
        userRepository.save(user);
        tokenRepository.save(new ConfirmationToken(token, user));
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setFrom("API");
        msg.setText(text);
        msg.setSubject(subject);

        mailService.sendMail(msg);
    }

    @Transactional
    public void confirm(String email, String token) {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()) throw new IllegalArgumentException("Bad username");
        ConfirmationToken confirmationToken = tokenRepository.findByUserAndToken(user.get(), token);
        if (null == confirmationToken) throw new IllegalArgumentException("Bad username or token");
        if (!TokenStatus.PENDING.equals(confirmationToken.getTokenStatus())) throw new IllegalStateException("Token expired or inactive");

        user.get().setActive(true);
        confirmationToken.setTokenStatus(TokenStatus.COMPLETED);
    }

    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email).get();
        String token = tokenGenerator.generateToken();
        String text = "Somebody requested password reset for your account. If you wish to reset password please click link below:"
                + "http://localhost:8080/resetPassword?user=" + user.getEmail() + "&token=" + token;
        String subject = "Confirm registration";
        tokenRepository.save(new ConfirmationToken(token, user));
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setFrom("API");
        msg.setText(text);
        msg.setSubject(subject);

        mailService.sendMail(msg);
    }
}

package com.example.services;

import com.example.dictionaries.TokenStatus;
import com.example.dictionaries.TokenType;
import com.example.factories.ConfirmationTokenFactory;
import com.example.factories.MailMessageFactory;
import com.example.models.ConfirmationToken;
import com.example.models.Mail;
import com.example.models.PasswordResetRequest;
import com.example.models.User;
import com.example.repositories.ConfirmationTokenRepository;
import com.example.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class AuthServiceTest {

    @MockBean
    private MailService mailService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ConfirmationTokenRepository tokenRepository;
    @MockBean
    private ConfirmationTokenFactory confirmationTokenFactory;
    private final MailMessageFactory mailMessageBuilder = new MailMessageFactory();
    private AuthService authService;

    private User savedUser;
    private User unsavedUser;
    private ConfirmationToken confirmationToken;
    private ConfirmationToken passwordToken;
    private PasswordResetRequest passwordResetRequest;
    private ConfirmationToken expiredConfirmationToken;
    private ConfirmationToken expiredPasswordToken;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<Mail> mailCaptor;

    @Before
    public void setUp() throws Exception {
        savedUser = new User("secret", "Chuck", "Norris", "chuck@example.com");
        unsavedUser = new User("secret", "Chuck", "Norris", "new_user@example.com");
        confirmationToken = new ConfirmationToken("token123", savedUser, TokenType.EMAIL_CONFIRMATION, new Date(1), TokenStatus.PENDING);
        passwordToken = new ConfirmationToken("token123", savedUser, TokenType.PASSWORD_RESET, new Date(1), TokenStatus.PENDING);
        expiredConfirmationToken = new ConfirmationToken(confirmationToken.getToken(), savedUser, TokenType.EMAIL_CONFIRMATION, new Date(1), TokenStatus.EXPIRED);
        expiredPasswordToken = new ConfirmationToken(confirmationToken.getToken(), savedUser, TokenType.PASSWORD_RESET, new Date(1), TokenStatus.EXPIRED);
        passwordResetRequest = new PasswordResetRequest(savedUser.getEmail(), confirmationToken.getToken(), "newsecret");

        when(confirmationTokenFactory.create(unsavedUser, TokenType.EMAIL_CONFIRMATION)).then(invocation -> confirmationToken);
        when(confirmationTokenFactory.create(savedUser, TokenType.PASSWORD_RESET)).then(invocation -> passwordToken);
        when(userRepository.findByEmail(savedUser.getEmail())).then(invocation -> Optional.ofNullable(savedUser));
        when(userRepository.findByEmail(unsavedUser.getEmail())).then(invocation -> Optional.empty());
        when(tokenRepository.findByUserAndTokenAndTokenType(any(), anyString(), any())).then(invocation -> Optional.empty());
        when(tokenRepository.findByUserAndToken(savedUser, confirmationToken.getToken())).then(invocation -> Optional.ofNullable(confirmationToken));
        when(tokenRepository.findByUserAndTokenAndTokenType(savedUser, confirmationToken.getToken(), confirmationToken.getTokenType())).then(invocation -> Optional.ofNullable(confirmationToken));
        when(tokenRepository.findByUserAndTokenAndTokenType(savedUser, passwordToken.getToken(), passwordToken.getTokenType())).then(invocation -> Optional.ofNullable(passwordToken));

        this.authService = new AuthService(userRepository, mailService, tokenRepository, confirmationTokenFactory, mailMessageBuilder, new BCryptPasswordEncoder());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void register_method_should_save_user() throws Exception {
        this.authService.register(unsavedUser);
        verify(userRepository).save(unsavedUser);
    }

    @Test
    public void register_method_should_save_token() throws Exception {
        this.authService.register(unsavedUser);
        verify(tokenRepository).save(confirmationToken);
    }

    @Test
    public void register_method_should_send_email() throws Exception {
        this.authService.register(unsavedUser);
        verify(mailService).sendMail(mailCaptor.capture());
        Mail mail = mailCaptor.getValue();
        Map<String, Object> model = mail.getModel();
        User user = (User)model.get("user");
        assertEquals(user, unsavedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirm_method_should_throw_exception_when_user_not_found() throws Exception {
        when(userRepository.findByEmail("fakeUser@example.com")).then(invocation -> Optional.empty());
        this.authService.confirm("fakeUser@example.com", confirmationToken.getToken());
    }

    @Test
    public void confirm_method_should_search_for_a_token() throws Exception {
        this.authService.confirm(savedUser.getEmail(), confirmationToken.getToken());
        verify(tokenRepository).findByUserAndTokenAndTokenType(savedUser, confirmationToken.getToken(), TokenType.EMAIL_CONFIRMATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirm_method_should_throw_exception_when_token_not_found() throws Exception {
        this.authService.confirm(savedUser.getEmail(), "fakeToken");
    }

    @Test(expected = IllegalStateException.class)
    public void confirm_method_should_throw_exception_when_token_status_is_not_pending() throws Exception {
        when(tokenRepository.findByUserAndTokenAndTokenType(savedUser, confirmationToken.getToken(), TokenType.EMAIL_CONFIRMATION)).then(invocation -> Optional.ofNullable(expiredConfirmationToken));
        this.authService.confirm(savedUser.getEmail(), confirmationToken.getToken());
    }

    @Test
    public void confirm_method_should_activate_user() throws Exception {
        this.authService.confirm(savedUser.getEmail(), confirmationToken.getToken());
        savedUser.setActive(true);
        verify(userRepository).save(savedUser);
    }

    @Test
    public void confirm_method_should_change_token_status_to_completed() throws Exception {
        this.authService.confirm(savedUser.getEmail(), confirmationToken.getToken());
        confirmationToken.setTokenStatus(TokenStatus.COMPLETED);
        verify(tokenRepository).save(confirmationToken);
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirm_method_should_throw_exception_when_token_type_is_inappropriate() throws Exception {
        when(tokenRepository.findByUserAndTokenAndTokenType(savedUser, confirmationToken.getToken(), TokenType.EMAIL_CONFIRMATION)).then(invocation -> Optional.empty());
        this.authService.confirm(savedUser.getEmail(), confirmationToken.getToken());
        confirmationToken.setTokenStatus(TokenStatus.COMPLETED);
        verify(tokenRepository).save(confirmationToken);
    }

    @Test
    public void requestPasswordReset_should_save_token() {
        this.authService.requestPasswordReset(savedUser.getEmail());
        confirmationToken.setTokenType(TokenType.PASSWORD_RESET);
        verify(tokenRepository).save(confirmationToken);
    }

    @Test
    public void requestPasswordReset_should_send_email() {
        this.authService.requestPasswordReset(savedUser.getEmail());
        verify(mailService).sendMail(mailCaptor.capture());
        Mail mail = mailCaptor.getValue();
        Map<String, Object> model = mail.getModel();
        User user = (User)model.get("user");
        assertEquals(user, savedUser);
    }

    @Test
    public void passwordReset_should_change_user_password() {
        savedUser.setPassword(passwordResetRequest.getPassword());
        this.authService.passwordReset(passwordResetRequest);
        verify(userRepository).save(savedUser);
    }

    @Test(expected = IllegalStateException.class)
    public void passwordReset_should_fail_when_token_status_is_not_pending() {
        when(tokenRepository.findByUserAndTokenAndTokenType(savedUser, confirmationToken.getToken(), TokenType.PASSWORD_RESET)).then(invocation -> Optional.ofNullable(expiredPasswordToken));
        this.authService.passwordReset(passwordResetRequest);
        verify(userRepository, never()).save(userArgumentCaptor.capture());
    }

    @Test(expected = IllegalArgumentException.class)
    public void passwordReset_should_fail_when_token_does_not_match() {
        passwordResetRequest.setToken("faketoken");
        this.authService.passwordReset(passwordResetRequest);
        verify(userRepository, never()).save(userArgumentCaptor.capture());
    }
}
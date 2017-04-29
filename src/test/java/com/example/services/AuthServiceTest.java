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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;

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

    private User user;
    private ConfirmationToken confirmationToken;
    private ConfirmationToken passwordToken;
    private PasswordResetRequest passwordResetRequest;
    private ConfirmationToken expiredConfirmationToken;
    private ConfirmationToken expiredPasswordToken;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        user = new User("secret", "Chuck", "Norris", "chuck@example.com");
        confirmationToken = new ConfirmationToken("token123", user, TokenType.EMAIL_CONFIRMATION, new Date(1), TokenStatus.PENDING);
        passwordToken = new ConfirmationToken("token123", user, TokenType.PASSWORD_RESET, new Date(1), TokenStatus.PENDING);
        expiredConfirmationToken = new ConfirmationToken(confirmationToken.getToken(), user, TokenType.EMAIL_CONFIRMATION, new Date(1), TokenStatus.EXPIRED);
        expiredPasswordToken = new ConfirmationToken(confirmationToken.getToken(), user, TokenType.PASSWORD_RESET, new Date(1), TokenStatus.EXPIRED);
        passwordResetRequest = new PasswordResetRequest(user.getEmail(), confirmationToken.getToken(), "newsecret");

        when(confirmationTokenFactory.create(user, TokenType.EMAIL_CONFIRMATION)).then(invocation -> confirmationToken);
        when(confirmationTokenFactory.create(user, TokenType.PASSWORD_RESET)).then(invocation -> passwordToken);
        when(userRepository.findByEmail(user.getEmail())).then(invocation -> Optional.ofNullable(user));
        when(tokenRepository.findByUserAndTokenAndTokenType(any(), anyString(), any())).then(invocation -> Optional.empty());
        when(tokenRepository.findByUserAndToken(user, confirmationToken.getToken())).then(invocation -> Optional.ofNullable(confirmationToken));
        when(tokenRepository.findByUserAndTokenAndTokenType(user, confirmationToken.getToken(), confirmationToken.getTokenType())).then(invocation -> Optional.ofNullable(confirmationToken));
        when(tokenRepository.findByUserAndTokenAndTokenType(user, passwordToken.getToken(), passwordToken.getTokenType())).then(invocation -> Optional.ofNullable(passwordToken));

        this.authService = new AuthService(userRepository, mailService, tokenRepository, confirmationTokenFactory, mailMessageBuilder);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void register_method_should_save_user() throws Exception {
        this.authService.register(user);
        verify(userRepository).save(user);
    }

    @Test
    public void register_method_should_save_token() throws Exception {
        this.authService.register(user);
        verify(tokenRepository).save(new ConfirmationToken(confirmationToken.getToken(), user, TokenType.EMAIL_CONFIRMATION, new Date(1)));
    }

    @Test
    public void register_method_should_send_email() throws Exception {
        this.authService.register(user);
        String text = "Somebody registered your email on our site. If you wish to confirm registration please click on link below:"
                + "http://localhost:8080/auth/confirm?user=" + user.getEmail() + "&token=token123";

        verify(mailService).sendMail(mailMessageBuilder.create().to(user.getEmail()).from("API").subject("Confirm registration").text(text).build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirm_method_should_throw_exception_when_user_not_found() throws Exception {
        when(userRepository.findByEmail("fakeUser@example.com")).then(invocation -> Optional.empty());
        this.authService.confirm("fakeUser@example.com", confirmationToken.getToken());
    }

    @Test
    public void confirm_method_should_search_for_a_token() throws Exception {
        this.authService.confirm(user.getEmail(), confirmationToken.getToken());
        verify(tokenRepository).findByUserAndTokenAndTokenType(user, confirmationToken.getToken(), TokenType.EMAIL_CONFIRMATION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirm_method_should_throw_exception_when_token_not_found() throws Exception {
        this.authService.confirm(user.getEmail(), "fakeToken");
    }

    @Test(expected = IllegalStateException.class)
    public void confirm_method_should_throw_exception_when_token_status_is_not_pending() throws Exception {
        when(tokenRepository.findByUserAndTokenAndTokenType(user, confirmationToken.getToken(), TokenType.EMAIL_CONFIRMATION)).then(invocation -> Optional.ofNullable(expiredConfirmationToken));
        this.authService.confirm(user.getEmail(), confirmationToken.getToken());
    }

    @Test
    public void confirm_method_should_activate_user() throws Exception {
        this.authService.confirm(user.getEmail(), confirmationToken.getToken());
        user.setActive(true);
        verify(userRepository).save(user);
    }

    @Test
    public void confirm_method_should_change_token_status_to_completed() throws Exception {
        this.authService.confirm(user.getEmail(), confirmationToken.getToken());
        confirmationToken.setTokenStatus(TokenStatus.COMPLETED);
        verify(tokenRepository).save(confirmationToken);
    }

    @Test(expected = IllegalArgumentException.class)
    public void confirm_method_should_throw_exception_when_token_type_is_inappropriate() throws Exception {
        when(tokenRepository.findByUserAndTokenAndTokenType(user, confirmationToken.getToken(), TokenType.EMAIL_CONFIRMATION)).then(invocation -> Optional.empty());
        this.authService.confirm(user.getEmail(), confirmationToken.getToken());
        confirmationToken.setTokenStatus(TokenStatus.COMPLETED);
        verify(tokenRepository).save(confirmationToken);
    }

    @Test
    public void requestPasswordReset_should_save_token() {
        this.authService.requestPasswordReset(user.getEmail());
        confirmationToken.setTokenType(TokenType.PASSWORD_RESET);
        verify(tokenRepository).save(confirmationToken);
    }

    @Test
    public void requestPasswordReset_should_send_email() {
        this.authService.requestPasswordReset(user.getEmail());
        String text = "Somebody requested password reset for your account. If you wish to reset password please click link below:"
                + "http://localhost:8080/resetPassword?user=" + user.getEmail() + "&token=" + confirmationToken.getToken();
        String subject = "Confirm registration";
        verify(mailService).sendMail(mailMessageBuilder.create().to(user.getEmail()).from("API").text(text).subject(subject).build());
    }

    @Test
    public void passwordReset_should_change_user_password() {
        user.setPassword(passwordResetRequest.getPassword());
        this.authService.passwordReset(passwordResetRequest);
        verify(userRepository).save(user);
    }

    @Test(expected = IllegalStateException.class)
    public void passwordReset_should_fail_when_token_status_is_not_pending() {
        when(tokenRepository.findByUserAndTokenAndTokenType(user, confirmationToken.getToken(), TokenType.PASSWORD_RESET)).then(invocation -> Optional.ofNullable(expiredPasswordToken));
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
package com.example.controllers;

import com.example.models.PasswordResetRequest;
import com.example.models.User;
import com.example.repositories.UserRepository;
import com.example.services.AuthService;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AuthController.class, secure = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();
    private User user;
    private User notValidUser;
    private PasswordResetRequest passwordResetRequest;
    private PasswordResetRequest invalidPasswordResetRequest = new PasswordResetRequest();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @MockBean
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        user = new User("secret", "Chuck", "Norris", "chuck@example.com");
        notValidUser = new User("secret", "chuck@example.com");
        passwordResetRequest = new PasswordResetRequest("chuck@example.com", "token123", "123456789");
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void auth_register_should_call_auth_service_register() throws Exception {
        this.mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isNoContent());
        user.setActive(false);
        verify(authService).register(user);
    }

    @Test
    public void auth_confirm_should_call_auth_service_confirm() throws Exception {
        this.mockMvc.perform(get("/auth/confirm?token=token123&email=chuck@example.com"))
                .andExpect(status().isNoContent());
        verify(authService).confirm("chuck@example.com", "token123");
    }

    @Test
    public void auth_requestPasswordReset_should_call_auth_service() throws Exception {
        this.mockMvc.perform(get("/auth/requestPasswordReset?email=chuck@example.com"))
                .andExpect(status().isCreated());
        verify(authService).requestPasswordReset("chuck@example.com");
    }

    @Test
    public void auth_passwordReset_should_call_auth_service() throws Exception {
        this.mockMvc.perform(post("/auth/passwordReset")
                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(passwordResetRequest)))
                .andExpect(status().isNoContent());
        verify(authService).passwordReset(passwordResetRequest);
    }

    @Test
    public void auth_passwordReset_should_validate() throws Exception {
        this.mockMvc.perform(post("/auth/passwordReset")
                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(invalidPasswordResetRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void auth_register_should_validate_user() throws Exception {
        this.mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(notValidUser)))
                .andExpect(status().is4xxClientError());
    }

}

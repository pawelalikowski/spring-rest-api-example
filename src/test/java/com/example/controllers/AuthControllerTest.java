package com.example.controllers;

import com.example.models.ChuckNorris;
import com.example.models.PasswordResetRequest;
import com.example.models.User;
import com.example.repositories.ChuckNorrisRepository;
import com.example.services.AuthService;
import com.example.services.UserService;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AuthController.class, secure = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();
    private User user = new User("secret", "Chuck", "Norris", "chuck@example.com");
    private User notValidUser = new User("secret", "chuck@example.com");
    private PasswordResetRequest passwordResetRequest = new PasswordResetRequest("chuck@example.com", "token123", "123456789");
    private PasswordResetRequest invalidPasswordResetRequest = new PasswordResetRequest();

    @MockBean
    private AuthService authService;

    @Before
    public void setUp() throws Exception {
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void auth_register_should_call_auth_service_register() throws Exception {
        this.mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(user)))
                .andExpect(status().isCreated());
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

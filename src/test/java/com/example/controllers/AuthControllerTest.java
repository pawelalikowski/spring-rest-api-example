package com.example.controllers;

import com.example.models.ChuckNorris;
import com.example.models.User;
import com.example.repositories.ChuckNorrisRepository;
import com.example.services.AuthService;
import com.example.services.UserService;
import com.github.javafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    private MultiValueMap<String, String> requestParams;

    @Before
    public void setUp() throws Exception {
        final Faker faker = new Faker(new Locale("en"));
        requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", "Chuck");
        requestParams.add("password", "Chuck");
        requestParams.add("email", "chuck@communications.com");
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void auth_register_shoud_call_auth_service_register() throws Exception {
        this.mockMvc.perform(post("/auth/register").params(requestParams))
                .andExpect(status().isCreated());
        verify(authService).register(new User());
    }
}

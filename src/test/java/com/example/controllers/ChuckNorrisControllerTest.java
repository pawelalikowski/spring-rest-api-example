package com.example.controllers;

import com.example.models.ChuckNorris;
import com.example.repositories.ChuckNorrisRepository;
import com.github.javafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ChuckNorrisController.class)
public class ChuckNorrisControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChuckNorrisRepository repository;

    List<ChuckNorris> list = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        final Faker faker = new Faker(new Locale("en"));
        for(int i = 1; i <= 5; i++) list.add(new ChuckNorris(i, faker.chuckNorris().fact()));
        when(repository.findAll()).thenReturn(list);
    }

    @After
    public void tearDown() throws Exception {
        list = new ArrayList<>();
    }

    @Test
    public void chuckNorris_should_be_authenticated() throws Exception {
        this.mockMvc
                .perform(get("/chuckNorris"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    public void chuckNorris_should_return_ok() throws Exception {
            this.mockMvc
                    .perform(get("/chuckNorris"))
                    .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void chuckNorris_should_render_first_fact() throws Exception {
            this.mockMvc.perform(get("/chuckNorris"))
                    .andExpect(content().string(containsString(list.get(0).getFact())));
    }
}
package com.example.controllers;

import com.example.models.ChuckNorris;
import com.example.models.User;
import com.example.repositories.ChuckNorrisRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void index() throws Exception {
    }

    @Test
    public void show() throws Exception {
//        assertEquals(1, 2);
    }

    @Test
    public void create() throws Exception {
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void delete() throws Exception {
    }

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {
            this.mockMvc.perform(get("/chuckNorris")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string(containsString(list.get(0).getFact())));
    }
}
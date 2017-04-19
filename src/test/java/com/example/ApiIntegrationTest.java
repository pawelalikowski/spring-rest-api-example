package com.example;

import com.example.conf.AuthorizationServer;
import com.example.conf.RepositoryConf;
import com.example.conf.WebSecurity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ApiApplication.class, AuthorizationServer.class, RepositoryConf.class, WebSecurity.class},
        value = {"application.yml"})
public class ApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
//        Mockito.reset(mockedExternalServiceAuthenticator, mockedServiceGateway);
    }

    @Test
    public void health_endpoint_is_available_to_everyone() {
        when().get("/health")
                .then().statusCode(HttpStatus.OK.value()).body("status", equalTo("UP"));
    }

    @Test
    public void options_requests_are_available_to_everyone() {
        when().options("/oauth/token")
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    public void getting_stuff_without_token_returns_unauthorized() {
        given().contentType(ContentType.JSON)
                .when().get("/chuckNorris")
                .then().statusCode(HttpStatus.FORBIDDEN.value());
    }
}
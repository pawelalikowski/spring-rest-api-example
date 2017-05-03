package com.example;

import com.codahale.metrics.graphite.GraphiteReporter;
import com.example.conf.*;
import com.example.models.User;
import com.example.services.MailService;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                ApiApplication.class,
                AuthorizationServer.class,
                RepositoryConf.class,
                WebSecurity.class,
                AppBeans.class,

        },
        value = {"application.yml"})
public class ApiIntegrationTest {

    @MockBean
    public MailService mailService;

    @MockBean
    public GraphiteReporter graphiteReporter;

    @LocalServerPort
    private int port;

    private String token;
    private Gson gson = new Gson();
    private User user1 = new User("secret", "Chuck", "Norris", "chuck1@example.com");
    private User user2 = new User("secret", "Chuck", "Norris", "chuck2@example.com");


    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
//        Mockito.reset(mockedExternalServiceAuthenticator, mockedServiceGateway);
        ResponseBody body = RestAssured
                .given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic YWNtZTphY21lc2VjcmV0")
                .formParam("grant_type", "password")
                .formParam("username", "chuck@example.com")
                .formParam("password", "password")
                .request().post("/token").getBody();
        token = body.jsonPath().get("access_token").toString();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void options_requests_are_available_to_everyone() {
        when().options("/token")
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    public void getting_stuff_without_token_returns_unauthorized() {
        when().get("/chuckNorris")
                .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void getting_stuff_with_token_returns_ok() {
        given().header("Authorization", "Bearer " + token)
                .when().get("/chuckNorris")
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    public void auth_register_should_be_available_to_everyone() {
        given().contentType(ContentType.JSON).body(gson.toJson(user1))
                .when().post("/auth/register")
                .then().statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void auth_register_should_check_is_email_taken() {
        given().contentType(ContentType.JSON).body(gson.toJson(user2))
                .when().post("/auth/register")
                .then().statusCode(HttpStatus.CREATED.value());
       given().contentType(ContentType.JSON).body(gson.toJson(user2))
                .when().post("/auth/register")
                .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

}

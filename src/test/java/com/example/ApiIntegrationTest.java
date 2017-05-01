package com.example;

import com.codahale.metrics.graphite.GraphiteReporter;
import com.example.conf.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

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
                AppBeans.class
        },
        value = {"application.yml"})
public class ApiIntegrationTest {

    @MockBean
    public GraphiteReporter graphiteReporter;

    @LocalServerPort
    private int port;
    private String token;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
//        Mockito.reset(mockedExternalServiceAuthenticator, mockedServiceGateway);
        token = RestAssured
                .given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic YWNtZTphY21lc2VjcmV0")
                .formParam("grant_type", "password")
                .formParam("username", "user")
                .formParam("password", "password")
                .request().post("/oauth/token").getBody().jsonPath().get("access_token").toString();
    }

    @Test
    public void options_requests_are_available_to_everyone() {
        when().options("/oauth/token")
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
    public void auth_register_should_be_available_to_everyone() throws Exception {
        String requestBody = "{\"username\": \"Chuck\","
                + "\"password\": \"Chuck\","
                + "\"email\": \"chuck@communications.com\"}";

        given().contentType(ContentType.JSON).body(requestBody)
                .when().post("/auth/register")
                .then().statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void auth_register_should_check_is_email_taken() throws Exception {
        String requestBody = "{\"username\": \"Chuck\","
                + "\"password\": \"Chuck\","
                + "\"email\": \"chuck@communications.com\"}";

        given().contentType(ContentType.JSON).body(requestBody)
                .when().post("/auth/register")
                .then().statusCode(HttpStatus.CREATED.value());
       given().contentType(ContentType.JSON).body(requestBody)
                .when().post("/auth/register")
                .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

}

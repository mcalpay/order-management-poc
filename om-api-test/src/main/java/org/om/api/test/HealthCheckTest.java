package org.om.api.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthCheckTest {

    private int port = 8080;
    private final String username = "user";
    private final String password = "password";

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testHealthEndpointWithValidCredentials() {
        given()
                .auth()
                .basic(username, password)
                .when()
                .get("/actuator/health")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("status", equalTo("UP"));
    }

    @Test
    public void testHealthEndpointWithInvalidCredentials() {
        given()
                .auth()
                .basic(username, "wrongpassword")
                .when()
                .get("/actuator/health")
                .then()
                .statusCode(401);
    }

    @Test
    public void testHealthEndpointWithValidCredentialsDetailedAssertion() {
        // This test gets the full response and makes multiple assertions
        Response response = given()
                .auth()
                .basic(username, password)
                .when()
                .get("/actuator/health")
                .then()
                .extract()
                .response();

        assertEquals(200, response.statusCode());
        assertEquals("UP", response.jsonPath().getString("status"));
    }

}

package org.om.api.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class OrderDeleteTest {

    public static final String CREATE_ROUTE = "/api/v1/om/create";
    public static final String DELETE_ROUTE = "/api/v1/om/";
    private final String password = "admin";

    @BeforeEach
    public void setUp() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
    }

    private static String getOrderJson(int customerId, String assetName, String orderSide, int size, int price) {
        return """
            {
              "customerId": %d,
              "assetName": "%s",
              "orderSide": "%s",
              "size": %d,
              "price": %d
            }
            """.formatted(customerId, assetName, orderSide, size, price);
    }

    @Test
    public void adminCanDeleteOrderId() {
        Response response = given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "SELL", 1, 2000))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(201)
                .body("status", equalTo("PENDING"))
                .extract().response();

        String orderId = response.jsonPath().getString("id");
        log.info("Response {}", response.asPrettyString());
        log.info("orderId {}", orderId);

        given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .when()
                .delete(DELETE_ROUTE + orderId)
                .then()
                .statusCode(200)
                .extract().response();
    }

    @Test
    public void adminCanNotDeleteInvalidOrderId() {
        Response response = given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .when()
                .delete(DELETE_ROUTE + "0")
                .then()
                .statusCode(400)
                .extract().response();
        log.info("Response {}", response.asPrettyString());
    }

    @Test
    public void userCanDeleteItsOrderId() {
        Response response = given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "SELL", 1, 2000))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(201)
                .body("status", equalTo("PENDING"))
                .extract().response();

        String orderId = response.jsonPath().getString("id");
        log.info("Response {}", response.asPrettyString());
        log.info("orderId {}", orderId);

        given()
                .auth()
                .basic("user", password)
                .contentType(ContentType.JSON)
                .when()
                .delete(DELETE_ROUTE + orderId)
                .then()
                .statusCode(200)
                .extract().response();
    }

    @Test
    public void userCanNotDeleteOtherUsersOrderId() {
        Response response = given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "SELL", 1, 2000))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(201)
                .body("status", equalTo("PENDING"))
                .extract().response();

        String orderId = response.jsonPath().getString("id");
        log.info("Response {}", response.asPrettyString());
        log.info("orderId {}", orderId);

        given()
                .auth()
                .basic("investor", password)
                .contentType(ContentType.JSON)
                .when()
                .delete(DELETE_ROUTE + orderId)
                .then()
                .statusCode(403)
                .extract().response();
    }

}

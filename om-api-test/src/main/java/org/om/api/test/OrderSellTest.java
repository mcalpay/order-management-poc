package org.om.api.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class OrderSellTest {

    public static final String CREATE_ROUTE = "/api/v1/om/create";
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
    public void canNotCreateOrderWithoutAuthorization() {
        given()
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "SELL", 10, 2000))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(401);
    }

    @Test
    public void adminCanCreateOrderThenStatusIsPending() {
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

        log.info("Response {}", response.asPrettyString());
    }

    @Test
    public void canNotSellTheAssetUserDoesNotHave() {
        Response response = given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "XXX", "SELL", 10, 2000))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(400)
                .body(equalTo("Customer does not have asset!"))
                .extract().response();

        log.info("Response {}", response.asPrettyString());
    }

    @Test
    public void canNotSellIfUserDoesNotHaveEnoughOfAsset() {
        Response response = given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "SELL", 10000, 2000))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(400)
                .body(equalTo("Customer does not have asset!"))
                .extract().response();

        log.info("Response {}", response.asPrettyString());
    }

    @Disabled
    @Test
    public void canNotSellIfUserDoesNotHaveEnoughOfAssetAfterPreviousPendingOffers() {
        Response response = given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "SELL", 600, 2000))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(201)
                .extract().response();
        log.info("Response {}", response.asPrettyString());

        response = given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "SELL", 600, 2000))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(400)
                .body(equalTo("Customer does not have asset!"))
                .extract().response();

        log.info("Response {}", response.asPrettyString());
    }

    @Test
    public void userCanNotCreateOrderForOtherUser() {
        Response response = given()
                .auth()
                .basic("trader", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "SELL", 1, 2000))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(403)
                .extract().response();

        log.info("Response {}", response.asPrettyString());
    }

    @Test
    public void usersCanCreateOrderForThemSelves() {
        Response response = given()
                .auth()
                .basic("user", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "SELL", 1, 2000))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(201)
                .extract().response();

        log.info("Response {}", response.asPrettyString());
    }

}

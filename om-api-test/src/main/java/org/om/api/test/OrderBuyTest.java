package org.om.api.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class OrderBuyTest {

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
    public void canNotBuyIfUserDoesNotHaveTRY() {
        Response response = given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "BUY", 10, 2000))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(400)
                .body(equalTo("Customer does not have asset!"))
                .extract().response();

        log.info("Response {}", response.asPrettyString());
    }

    @Test
    public void canBuyIfUserHaveTRY() {
        Response response = given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "BUY", 10, 1))
                .when()
                .post(CREATE_ROUTE)
                .then()
                .statusCode(201)
                .extract().response();

        log.info("Response {}", response.asPrettyString());
    }

    @Disabled
    @Test
    public void buyOrdersReduceTheTRYAssetSize() {
        RequestSpecification largeBuyRequest = given()
                .auth()
                .basic("admin", password)
                .contentType(ContentType.JSON)
                .body(getOrderJson(2, "WV", "BUY", 600, 1))
                .when();
        largeBuyRequest
                .post(CREATE_ROUTE)
                .then()
                .statusCode(201)
                .extract().response();
        Response response = largeBuyRequest
                .post(CREATE_ROUTE)
                .then()
                .statusCode(400)
                .extract().response();
        log.info("Response {}", response.asPrettyString());
    }

}

package io.github.yashchenkon.banking.api.rest.account;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.github.yashchenkon.banking.api.rest.account.body.CreateAccountRequestV1_0;
import io.restassured.RestAssured;

import java.util.UUID;

public class AccountsRestApiTest extends BaseRestApiTest {

    @Test
    public void shouldReturnErrorWhenAccountDoesNotExist() {
        RestAssured
            .given()
            .when()
            .get("/accounts/{id}", UUID.randomUUID().toString())
            .then()
            .statusCode(404)
            .body("message", Matchers.equalTo("Cannot find such account"));
    }

    @Test
    public void shouldCreateAccount() {
        CreateAccountRequestV1_0 request = new CreateAccountRequestV1_0();
        request.setName(UUID.randomUUID().toString());
        request.setCurrency("USD");

        String id = RestAssured
            .given()
            .body(request)
            .when()
            .post("/accounts")
            .then()
            .statusCode(200)
            .body("id", Matchers.not(Matchers.blankOrNullString()))
            .extract()
            .response()
            .body()
            .jsonPath()
            .get("id");

        RestAssured
            .given()
            .when()
            .get("/accounts/{id}", id)
            .then()
            .statusCode(200)
            .body("iban", Matchers.equalTo(id))
            .body("name", Matchers.equalTo(request.getName()))
            .body("currency", Matchers.equalTo(request.getCurrency()))
            .body("balance", Matchers.equalTo(0.0f));
    }
}

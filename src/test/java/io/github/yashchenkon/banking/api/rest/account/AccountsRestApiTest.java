package io.github.yashchenkon.banking.api.rest.account;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.github.yashchenkon.banking.BaseOnlineBankingApplicationTest;
import io.restassured.RestAssured;

import java.util.UUID;

public class AccountsRestApiTest extends BaseOnlineBankingApplicationTest {

    @Test
    public void shouldReturnErrorWhenAccountDoesNotExist() {
        RestAssured
            .given()
            .spec(BASE_SPECIFICATION)
            .when()
            .get("/accounts/{id}", UUID.randomUUID().toString())
            .then()
            .statusCode(404)
            .body("message", Matchers.equalTo("Cannot find such account"));
    }
}

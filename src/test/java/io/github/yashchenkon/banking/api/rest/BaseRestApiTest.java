package io.github.yashchenkon.banking.api.rest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import io.github.yashchenkon.banking.OnlineBankingApplication;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import spark.Spark;

public abstract class BaseRestApiTest {

    public static RequestSpecification BASE_SPECIFICATION = new RequestSpecBuilder()
        .setBaseUri(baseUri())
        .setPort(port())
        .setBasePath(basePath())
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .build();

    @BeforeAll
    public static void startServer() {
        OnlineBankingApplication.main(new String[0]);

        RestAssured.requestSpecification = BASE_SPECIFICATION;
    }

    @AfterAll
    public static void stopServer() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static String baseUri() {
        return "http://localhost";
    }

    public static int port() {
        return 8080;
    }

    public static String basePath() {
        return "/api/v1.0";
    }
}

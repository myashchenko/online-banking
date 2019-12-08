package io.github.yashchenkon.banking;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import spark.Spark;

public class BaseOnlineBankingApplicationTest {

    public static RequestSpecification BASE_SPECIFICATION = new RequestSpecBuilder()
        .setBaseUri(baseUri())
        .setPort(port())
        .setBasePath(basePath())
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .log(LogDetail.ALL)
        .build();

    @BeforeAll
    public static void startServer() {
        OnlineBankingApplication.main(new String[0]);
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

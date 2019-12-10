package io.github.yashchenkon.banking.api.rest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import io.github.yashchenkon.banking.OnlineBankingApplication;
import io.github.yashchenkon.banking.infra.test.TimingExtension;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import spark.Spark;

import java.util.concurrent.TimeUnit;

@ExtendWith(TimingExtension.class)
public abstract class BaseRestApiTest {

    public static RequestSpecification BASE_SPECIFICATION = new RequestSpecBuilder()
        .setBaseUri(baseUri())
        .setPort(port())
        .setBasePath(basePath())
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .setConfig(
            // TODO it seems RestAssured doesn't reuse connections at all - it might cause problems when abnormal amount of connections are open at once
            RestAssuredConfig.config()
                .connectionConfig(ConnectionConfig.connectionConfig()
                    .closeIdleConnectionsAfterEachResponseAfter(1, TimeUnit.SECONDS)
                )
        )
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

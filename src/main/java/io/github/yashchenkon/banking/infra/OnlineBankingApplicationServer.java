package io.github.yashchenkon.banking.infra;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.yashchenkon.banking.api.rest.account.AccountsRestApiV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.TransactionsRestApiV1_0;
import io.github.yashchenkon.banking.infra.exception.OnlineBankingApplicationExceptionHandler;
import spark.Spark;

import javax.inject.Inject;

public class OnlineBankingApplicationServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineBankingApplicationServer.class);

    private final Gson gson;
    private final AccountsRestApiV1_0 accountsRestApi;
    private final TransactionsRestApiV1_0 transactionsRestApi;

    @Inject
    public OnlineBankingApplicationServer(Gson gson, AccountsRestApiV1_0 accountsRestApi, TransactionsRestApiV1_0 transactionsRestApi) {
        this.gson = gson;
        this.accountsRestApi = accountsRestApi;
        this.transactionsRestApi = transactionsRestApi;
    }

    public void run(int port) {
        LOGGER.info("Starting Online Banking Application...");

        Spark.exception(Exception.class, new OnlineBankingApplicationExceptionHandler(gson));
        Spark.initExceptionHandler(ex -> LOGGER.error(ex.getMessage()));

        Spark.port(port);

        Spark.before((request, response) -> response.type("application/json"));

        Spark.path("/api/v1.0", () -> {
            Spark.path("/accounts", () -> {
                Spark.post("/", accountsRestApi.create(), gson::toJson);
                Spark.get("/:id", accountsRestApi.getById(), gson::toJson);
            });

            Spark.path("/transactions", () -> {
                Spark.post("/", transactionsRestApi.create(), gson::toJson);
            });
        });

        Spark.awaitInitialization();

        LOGGER.info("Online Banking Application has been successfully started");
    }
}

package io.github.yashchenkon.banking.api.rest.account;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.github.yashchenkon.banking.api.rest.account.body.CreateAccountRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.DepositMoneyRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.TransferMoneyRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.WithdrawMoneyRequestV1_0;
import io.github.yashchenkon.banking.domain.model.transaction.TransactionType;
import io.restassured.RestAssured;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TransactionsRestApiTest extends BaseRestApiTest {

    @Test
    public void shouldDepositMoney() {
        String accountId = createRandomAccount();

        Double amount = 100.0;
        String transactionId = deposit(accountId, amount);

        verifyTransaction(transactionId, TransactionType.DEPOSIT, accountId, accountId, amount);
        verifyAccount(accountId, amount);
    }

    @Test
    public void shouldWithdrawMoney() {
        String accountId = createRandomAccount();
        Double initialAmount = 100.0;
        deposit(accountId, initialAmount);

        Double amountToWithdraw = 50.0;
        String transactionId = withdraw(accountId, amountToWithdraw);

        verifyTransaction(transactionId, TransactionType.WITHDRAW, accountId, accountId, amountToWithdraw);
        verifyAccount(accountId, initialAmount - amountToWithdraw);
    }

    @Test
    public void shouldTransferMoney() {
        String sourceAccountId = createRandomAccount();
        String targetAccountId = createRandomAccount();

        Double amount = 100.0;
        deposit(sourceAccountId, amount);

        String transactionId = transfer(sourceAccountId, targetAccountId, amount);

        verifyTransaction(transactionId, TransactionType.TRANSFER, sourceAccountId, targetAccountId, amount);
        verifyAccount(sourceAccountId, 0.0);
        verifyAccount(targetAccountId, amount);
    }

    @Test
    public void shouldTransferMoneyHighConcurrency() throws InterruptedException {
        String sourceAccountId = createRandomAccount();
        String targetAccountId = createRandomAccount();

        Double amount = 10000.0;
        deposit(sourceAccountId, amount);
        deposit(targetAccountId, amount);

        ScheduledThreadPoolExecutor threadPoolExecutor1 = new ScheduledThreadPoolExecutor(5);
        threadPoolExecutor1.prestartAllCoreThreads();

        ScheduledThreadPoolExecutor threadPoolExecutor2 = new ScheduledThreadPoolExecutor(5);
        threadPoolExecutor1.prestartAllCoreThreads();

        CountDownLatch readySteadyGo = new CountDownLatch(1);
        transferAsync(threadPoolExecutor1, readySteadyGo, sourceAccountId, targetAccountId, 1.0);
        transferAsync(threadPoolExecutor2, readySteadyGo, targetAccountId, sourceAccountId, 1.0);
        readySteadyGo.countDown();

        threadPoolExecutor1.shutdown();
        threadPoolExecutor1.awaitTermination(60L, TimeUnit.SECONDS);

        threadPoolExecutor2.shutdown();
        threadPoolExecutor2.awaitTermination(60L, TimeUnit.SECONDS);

        verifyAccount(sourceAccountId, amount);
        verifyAccount(targetAccountId, amount);
    }

    private void transferAsync(ScheduledThreadPoolExecutor threadPoolExecutor, CountDownLatch startAllTogether, String sourceAccountId, String targetAccountId, Double amount) throws InterruptedException {
        for (int i = 0; i < 5000; i++) {
            threadPoolExecutor.submit((Callable<Void>) () -> {
                startAllTogether.await();
                transfer(sourceAccountId, targetAccountId, amount);
                return null;
            });
        }
    }

    private String createRandomAccount() {
        CreateAccountRequestV1_0 request = new CreateAccountRequestV1_0();
        request.setName(UUID.randomUUID().toString());
        request.setCurrency("USD");

        return RestAssured
            .given()
            .body(request)
            .when()
            .post("/accounts")
            .then()
            .statusCode(200)
            .extract()
            .response()
            .body()
            .jsonPath()
            .get("id");
    }

    private String deposit(String accountId, Double amount) {
        DepositMoneyRequestV1_0 request = new DepositMoneyRequestV1_0();
        request.setTargetAccountId(accountId);
        request.setAmount(amount);

        return RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/deposit")
            .then()
            .statusCode(200)
            .body("id", Matchers.not(Matchers.blankOrNullString()))
            .extract()
            .response()
            .path("id");
    }

    private String withdraw(String accountId, Double amount) {
        WithdrawMoneyRequestV1_0 request = new WithdrawMoneyRequestV1_0();
        request.setTargetAccountId(accountId);
        request.setAmount(amount);

        return RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/withdraw")
            .then()
            .statusCode(200)
            .body("id", Matchers.not(Matchers.blankOrNullString()))
            .extract()
            .response()
            .path("id");
    }

    private String transfer(String sourceAccountId, String targetAccountId, Double amount) {
        TransferMoneyRequestV1_0 request = new TransferMoneyRequestV1_0();
        request.setSourceAccountId(sourceAccountId);
        request.setTargetAccountId(targetAccountId);
        request.setAmount(amount);

        return RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/transfer")
            .then()
            .statusCode(200)
            .body("id", Matchers.not(Matchers.blankOrNullString()))
            .extract()
            .response()
            .path("id");
    }

    private void verifyTransaction(String transactionId, TransactionType type, String sourceAccountId, String targetAccountId,
                                   Double amount) {
        RestAssured.given()
            .when()
            .get("/transactions/{id}", transactionId)
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo(transactionId))
            .body("type", Matchers.equalTo(type.name()))
            .body("sourceAccountId", Matchers.equalTo(sourceAccountId))
            .body("targetAccountId", Matchers.equalTo(targetAccountId))
            .body("amount", Matchers.equalTo(amount.floatValue()));
    }

    private void verifyAccount(String accountId, Double balance) {
        RestAssured.given()
            .when()
            .get("/accounts/{id}", accountId)
            .then()
            .statusCode(200)
            .body("balance", Matchers.equalTo(balance.floatValue()));
    }
}

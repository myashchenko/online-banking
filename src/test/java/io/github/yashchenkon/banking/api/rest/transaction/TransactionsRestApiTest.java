package io.github.yashchenkon.banking.api.rest.transaction;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.yashchenkon.banking.api.rest.BaseRestApiTest;
import io.github.yashchenkon.banking.api.rest.account.body.CreateAccountRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.DepositMoneyRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.TransferMoneyRequestV1_0;
import io.github.yashchenkon.banking.api.rest.transaction.body.WithdrawMoneyRequestV1_0;
import io.github.yashchenkon.banking.domain.model.transaction.TransactionType;
import io.restassured.RestAssured;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TransactionsRestApiTest extends BaseRestApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsRestApiTest.class);

    @Test
    public void shouldDepositMoney() {
        String accountId = createRandomAccount();

        Double amount = 100.0;
        String transactionId = deposit(accountId, amount);

        verifyTransaction(transactionId, TransactionType.DEPOSIT, accountId, accountId, amount);
        verifyAccount(accountId, amount);
    }

    @Test
    public void shouldReturnErrorOnDepositNegativeSum() {
        String accountId = createRandomAccount();

        Double amount = -100.0;

        DepositMoneyRequestV1_0 request = new DepositMoneyRequestV1_0();
        request.setTargetAccountId(accountId);
        request.setAmount(amount);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/deposit")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Field 'amount' should be a valid value"));

        verifyAccount(accountId, 0.0);
    }

    @Test
    public void shouldReturnErrorOnDepositBlankAccount() {
        String accountId = createRandomAccount();

        Double amount = -100.0;

        DepositMoneyRequestV1_0 request = new DepositMoneyRequestV1_0();
        request.setTargetAccountId(null);
        request.setAmount(amount);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/deposit")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Field 'targetAccountId' shouldn't be blank"));

        verifyAccount(accountId, 0.0);
    }

    @Test
    public void shouldReturnErrorOnDepositBlankAmount() {
        String accountId = createRandomAccount();

        DepositMoneyRequestV1_0 request = new DepositMoneyRequestV1_0();
        request.setTargetAccountId(accountId);
        request.setAmount(null);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/deposit")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Field 'amount' should be a valid value"));

        verifyAccount(accountId, 0.0);
    }

    @Test
    public void shouldReturnErrorOnDepositAccountDoesNotExist() {
        DepositMoneyRequestV1_0 request = new DepositMoneyRequestV1_0();
        request.setTargetAccountId(UUID.randomUUID().toString());
        request.setAmount(100.0);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/deposit")
            .then()
            .statusCode(404)
            .body("message", Matchers.equalTo("Cannot find such account"));
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
    public void shouldReturnMoneyOnWithdrawNegativeSum() {
        String accountId = createRandomAccount();
        Double initialAmount = 100.0;
        deposit(accountId, initialAmount);

        Double amountToWithdraw = -50.0;
        WithdrawMoneyRequestV1_0 request = new WithdrawMoneyRequestV1_0();
        request.setTargetAccountId(accountId);
        request.setAmount(amountToWithdraw);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/withdraw")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Field 'amount' should be a valid value"));

        verifyAccount(accountId, 100.0);
    }

    @Test
    public void shouldReturnErrorOnWithdrawSumGreaterThanAvailableBalance() {
        String accountId = createRandomAccount();
        Double initialAmount = 100.0;
        deposit(accountId, initialAmount);

        Double amountToWithdraw = 100.1;
        WithdrawMoneyRequestV1_0 request = new WithdrawMoneyRequestV1_0();
        request.setTargetAccountId(accountId);
        request.setAmount(amountToWithdraw);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/withdraw")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Account has a low balance. Cannot process this transaction"));

        verifyAccount(accountId, 100.0);
    }

    @Test
    public void shouldReturnMoneyOnWithdrawBlankAccount() {
        String accountId = createRandomAccount();
        Double initialAmount = 100.0;
        deposit(accountId, initialAmount);

        Double amountToWithdraw = -50.0;
        WithdrawMoneyRequestV1_0 request = new WithdrawMoneyRequestV1_0();
        request.setTargetAccountId(null);
        request.setAmount(amountToWithdraw);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/withdraw")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Field 'targetAccountId' shouldn't be blank"));
    }

    @Test
    public void shouldReturnMoneyOnWithdrawBlankAmount() {
        String accountId = createRandomAccount();
        Double initialAmount = 100.0;
        deposit(accountId, initialAmount);

        WithdrawMoneyRequestV1_0 request = new WithdrawMoneyRequestV1_0();
        request.setTargetAccountId(accountId);
        request.setAmount(null);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/withdraw")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Field 'amount' should be a valid value"));
    }

    @Test
    public void shouldReturnMoneyOnWithdrawAccountDoesNotExist() {
        WithdrawMoneyRequestV1_0 request = new WithdrawMoneyRequestV1_0();
        request.setTargetAccountId(UUID.randomUUID().toString());
        request.setAmount(100.0);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/withdraw")
            .then()
            .statusCode(404)
            .body("message", Matchers.equalTo("Cannot find such account"));
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
    @Tag("slow")
    public void shouldTransferMoneyHighConcurrency() {
        String sourceAccountId = createRandomAccount();
        String targetAccountId = createRandomAccount();

        Double amount = 10000.0;
        deposit(sourceAccountId, amount);
        deposit(targetAccountId, amount);

        List<ScheduledThreadPoolExecutor> executors = createExecutors(2, 5);

        CountDownLatch sync = new CountDownLatch(1);
        CountDownLatch latch1 = transferAsync(executors.get(0), sync, sourceAccountId, targetAccountId, 1.0, 5000);
        CountDownLatch latch2 = transferAsync(executors.get(1), sync, targetAccountId, sourceAccountId, 1.0, 5000);
        sync.countDown();

        Stream.of(latch1, latch2).forEach(latch -> {
            try {
                latch.await(200, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        shutdownExecutors(executors);

        verifyAccount(sourceAccountId, amount);
        verifyAccount(targetAccountId, amount);
    }

    @Test
    @Tag("slow")
    public void testAllOperationsTogetherHighConcurrency() {
        String sourceAccountId = createRandomAccount();
        String targetAccountId = createRandomAccount();

        Double amount = 10000.0;
        deposit(sourceAccountId, amount);
        deposit(targetAccountId, amount);

        List<ScheduledThreadPoolExecutor> executors = createExecutors(6, 2);

        CountDownLatch readySteadyGo = new CountDownLatch(1);
        CountDownLatch latch1 = transferAsync(executors.get(0), readySteadyGo, sourceAccountId, targetAccountId, 1.0, 2000);
        CountDownLatch latch2 = transferAsync(executors.get(1), readySteadyGo, targetAccountId, sourceAccountId, 1.0, 2000);
        CountDownLatch latch3 = depositAsync(executors.get(2), readySteadyGo, sourceAccountId, 1.0, 2000);
        CountDownLatch latch4 = depositAsync(executors.get(3), readySteadyGo, targetAccountId, 1.0, 2000);
        CountDownLatch latch5 = withdrawAsync(executors.get(4), readySteadyGo, sourceAccountId, 1.0, 2000);
        CountDownLatch latch6 = withdrawAsync(executors.get(5), readySteadyGo, targetAccountId, 1.0, 2000);
        readySteadyGo.countDown();

        Stream.of(latch1, latch2, latch3, latch4, latch5, latch6).forEach(latch -> {
            try {
                latch.await(200, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        shutdownExecutors(executors);

        verifyAccount(sourceAccountId, amount);
        verifyAccount(targetAccountId, amount);
    }

    @Test
    @Tag("slow")
    public void shouldTransferMoneyHighConcurrencyCycle() {
        String account1 = createRandomAccount();
        String account2 = createRandomAccount();
        String account3 = createRandomAccount();

        Double amount = 10000.0;
        deposit(account1, amount);
        deposit(account2, amount);
        deposit(account3, amount);

        List<ScheduledThreadPoolExecutor> executors = createExecutors(3, 5);

        CountDownLatch sync = new CountDownLatch(1);
        CountDownLatch latch1 = transferAsync(executors.get(0), sync, account1, account2, 1.0, 2000);
        CountDownLatch latch2 = transferAsync(executors.get(0), sync, account1, account3, 1.0, 2000);
        CountDownLatch latch3 = transferAsync(executors.get(1), sync, account2, account3, 1.0, 2000);
        CountDownLatch latch4 = transferAsync(executors.get(1), sync, account2, account1, 1.0, 2000);
        CountDownLatch latch5 = transferAsync(executors.get(2), sync, account3, account1, 1.0, 2000);
        CountDownLatch latch6 = transferAsync(executors.get(2), sync, account3, account2, 1.0, 2000);
        sync.countDown();

        Stream.of(latch1, latch2, latch3, latch4, latch5, latch6).forEach(latch -> {
            try {
                latch.await(200, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        shutdownExecutors(executors);

        verifyAccount(account1, amount);
        verifyAccount(account2, amount);
        verifyAccount(account3, amount);
    }

    @Test
    public void shouldReturnErrorOnTransferMoneyNegativeValue() {
        String sourceAccountId = createRandomAccount();
        String targetAccountId = createRandomAccount();

        TransferMoneyRequestV1_0 request = new TransferMoneyRequestV1_0();
        request.setSourceAccountId(sourceAccountId);
        request.setTargetAccountId(targetAccountId);
        request.setAmount(-100.0);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/transfer")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Field 'amount' should be a valid value"));

        verifyAccount(sourceAccountId, 0.0);
        verifyAccount(targetAccountId, 0.0);
    }

    @Test
    public void shouldReturnErrorOnTransferMoneyLowBalance() {
        String sourceAccountId = createRandomAccount();
        String targetAccountId = createRandomAccount();

        TransferMoneyRequestV1_0 request = new TransferMoneyRequestV1_0();
        request.setSourceAccountId(sourceAccountId);
        request.setTargetAccountId(targetAccountId);
        request.setAmount(100.0);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/transfer")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Account has a low balance. Cannot process this transaction"));

        verifyAccount(sourceAccountId, 0.0);
        verifyAccount(targetAccountId, 0.0);
    }

    @Test
    public void shouldReturnErrorOnTransferSourceAccountBlank() {
        String targetAccountId = createRandomAccount();

        TransferMoneyRequestV1_0 request = new TransferMoneyRequestV1_0();
        request.setSourceAccountId(null);
        request.setTargetAccountId(targetAccountId);
        request.setAmount(100.0);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/transfer")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Field 'sourceAccountId' shouldn't be blank"));

        verifyAccount(targetAccountId, 0.0);
    }

    @Test
    public void shouldReturnErrorOnTransferTargetAccountBlank() {
        String sourceAccountId = createRandomAccount();

        TransferMoneyRequestV1_0 request = new TransferMoneyRequestV1_0();
        request.setSourceAccountId(sourceAccountId);
        request.setTargetAccountId(null);
        request.setAmount(100.0);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/transfer")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Field 'targetAccountId' shouldn't be blank"));

        verifyAccount(sourceAccountId, 0.0);
    }

    @Test
    public void shouldReturnErrorOnTransferTargetAmountBlank() {
        String sourceAccountId = createRandomAccount();
        String targetAccountId = createRandomAccount();

        TransferMoneyRequestV1_0 request = new TransferMoneyRequestV1_0();
        request.setSourceAccountId(sourceAccountId);
        request.setTargetAccountId(targetAccountId);
        request.setAmount(null);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/transfer")
            .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("Field 'amount' should be a valid value"));
    }

    @Test
    public void shouldReturnErrorOnTransferTargetAccountDoesNotExist() {
        String sourceAccountId = createRandomAccount();
        deposit(sourceAccountId, 100.0);

        TransferMoneyRequestV1_0 request = new TransferMoneyRequestV1_0();
        request.setSourceAccountId(sourceAccountId);
        request.setTargetAccountId(UUID.randomUUID().toString());
        request.setAmount(100.0);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/transfer")
            .then()
            .statusCode(404)
            .body("message", Matchers.equalTo("Cannot find such account"));

        verifyAccount(sourceAccountId, 100.0);
    }

    @Test
    public void shouldReturnErrorOnTransferSourceAccountDoesNotExist() {
        String targetAccountId = createRandomAccount();

        TransferMoneyRequestV1_0 request = new TransferMoneyRequestV1_0();
        request.setSourceAccountId(UUID.randomUUID().toString());
        request.setTargetAccountId(targetAccountId);
        request.setAmount(100.0);

        RestAssured.given()
            .body(request)
            .when()
            .post("/transactions/transfer")
            .then()
            .statusCode(404)
            .body("message", Matchers.equalTo("Cannot find such account"));

        verifyAccount(targetAccountId, 0.0);
    }

    private CountDownLatch transferAsync(ScheduledThreadPoolExecutor threadPoolExecutor, CountDownLatch startAllTogether, String sourceAccountId, String targetAccountId, Double amount, int numberOfTasks) {
        CountDownLatch latch = new CountDownLatch(numberOfTasks);
        for (int i = 0; i < numberOfTasks; i++) {
            threadPoolExecutor.submit((Callable<Void>) () -> {
                startAllTogether.await();
                transfer(sourceAccountId, targetAccountId, amount);
                latch.countDown();
                return null;
            });
        }
        return latch;
    }

    private CountDownLatch depositAsync(ScheduledThreadPoolExecutor threadPoolExecutor, CountDownLatch startAllTogether, String targetAccountId, Double amount, int numberOfTasks) {
        CountDownLatch latch = new CountDownLatch(numberOfTasks);
        for (int i = 0; i < numberOfTasks; i++) {
            threadPoolExecutor.submit((Callable<Void>) () -> {
                startAllTogether.await();
                deposit(targetAccountId, amount);
                latch.countDown();
                return null;
            });
        }
        return latch;
    }

    private CountDownLatch withdrawAsync(ScheduledThreadPoolExecutor threadPoolExecutor, CountDownLatch startAllTogether, String targetAccountId, Double amount, int numberOfTasks) {
        CountDownLatch latch = new CountDownLatch(numberOfTasks);
        for (int i = 0; i < numberOfTasks; i++) {
            threadPoolExecutor.submit((Callable<Void>) () -> {
                startAllTogether.await();
                withdraw(targetAccountId, amount);
                latch.countDown();
                return null;
            });
        }
        return latch;
    }

    private List<ScheduledThreadPoolExecutor> createExecutors(int count, int threads) {
        return IntStream.range(0, count)
            .mapToObj($ -> new ScheduledThreadPoolExecutor(threads))
            .peek(ThreadPoolExecutor::prestartAllCoreThreads)
            .collect(Collectors.toList());
    }

    private void shutdownExecutors(List<ScheduledThreadPoolExecutor> executors) {
        executors.forEach(executor -> {
            LOGGER.info("Shutting down executor");
            executor.shutdown();
            try {
                LOGGER.info("Awaiting shutting down executor");
                executor.awaitTermination(120L, TimeUnit.SECONDS);
                LOGGER.info("Awaiting shutting down executor has been done");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
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

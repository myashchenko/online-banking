# Online Banking Application

REST API for transferring money between bank accounts.

## Tech stack

- Java 11
- Gradle 6
- SparkJava / Gson / Guice
- JDBC / H2 Database
- Rest Assured / JUnit 5

## API

| Method | Endpoint                          | Payload                                                                                  | Description                                |
|--------|-----------------------------------|------------------------------------------------------------------------------------------|--------------------------------------------|
| POST   | `/api/v1.0/accounts`              | <code>{ "name" : "name", "currency" : "USD" }</code>                                     | Create new account                         |
| GET    | `/api/v1.0/accounts/{id}`         |                                                                                          | Get details about account                  |
| POST   | `/api/v1.0/transactions/deposit`  | <code>{ "targetAccountId" : "123", "amount" : 100.0 }</code>                             | Deposit money to account                   |
| POST   | `/api/v1.0/transactions/withdraw` | <code>{ "targetAccountId" : "123", "amount" : 100.0 }</code>                             | Withdraw money from account                |
| POST   | `/api/v1.0/transactions/transfer` | <code>{ "sourceAccountId" : "123", "targetAccountId" : "345", "amount" : 100.0 }</code>  | Transfer money from one account to another |

## Running
To start service just run:

```
./gradlew run
```

To run tests, execute the following command:

```
./gradlew test
```

**Please make sure you don't have anything running on 8080 port before starting application / running tests**


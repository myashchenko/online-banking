CREATE TABLE IF NOT EXISTS accounts(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    iban VARCHAR(32) NOT NULL,
    name VARCHAR(255) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    balance DECIMAL(13, 4) NOT NULL,
    UNIQUE KEY uk_accounts_iban(iban)
);
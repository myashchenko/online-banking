CREATE TABLE IF NOT EXISTS accounts(
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    balance DECIMAL(13, 4) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions(
    id VARCHAR(36) PRIMARY KEY,
    source_account_id VARCHAR(36) NOT NULL,
    target_account_id VARCHAR(36) NOT NULL,
    amount DECIMAL(13, 4) NOT NULL,
    completed_at TIMESTAMP NOT NULL,
    type VARCHAR(10) NOT NULL,

    CONSTRAINT fk_transactions_source_account_id FOREIGN KEY (source_account_id) REFERENCES accounts(id),
    CONSTRAINT fk_transactions_target_account_id FOREIGN KEY (target_account_id) REFERENCES accounts(id)
);
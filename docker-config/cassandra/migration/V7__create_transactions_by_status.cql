USE payment_processor;
-- Get transactions by status
CREATE TABLE IF NOT EXISTS transactions_by_status
(
    status           TEXT,
    transaction_date DATE,
    created_at       TIMESTAMP,
    transaction_id   UUID,
    card_id          UUID,
    amount           DECIMAL,

    PRIMARY KEY (
        (status, transaction_date),
         created_at,
         transaction_id
        )
)
    WITH CLUSTERING ORDER BY (
                              created_at DESC,
                              transaction_id ASC
        );
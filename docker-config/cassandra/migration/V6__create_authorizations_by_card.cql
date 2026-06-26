USE payment_processor;
-- Get recent authorizations by card
CREATE TABLE IF NOT EXISTS authorizations_by_card
(
    card_id            UUID,
    authorization_date DATE,
    transaction_id     UUID,
    amount             DECIMAL,
    merchant           TEXT,
    status             TEXT,
    created_at         TIMESTAMP,

    PRIMARY KEY (
        (card_id, authorization_date),
         created_at,
         transaction_id
        )
)
    WITH CLUSTERING ORDER BY (
                              created_at DESC,
                              transaction_id ASC
        );
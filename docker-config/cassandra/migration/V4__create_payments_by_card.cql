USE payment_processor;
-- Get payments history by card
CREATE TABLE IF NOT EXISTS payments_by_card
(
    card_id        UUID,
    payment_date   DATE,
    transaction_id UUID,
    amount         DECIMAL,
    currency       TEXT,
    status         TEXT,
    merchant       TEXT,
    created_at     TIMESTAMP,

    PRIMARY KEY (
        (card_id, payment_date),
         created_at,
         transaction_id
        )
)
    WITH CLUSTERING ORDER BY (
                              created_at DESC,
                              transaction_id ASC
        );
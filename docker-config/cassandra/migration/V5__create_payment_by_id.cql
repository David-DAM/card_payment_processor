USE payment_processor;
-- Get the status of a payment
CREATE TABLE IF NOT EXISTS payments
(
    transaction_id UUID PRIMARY KEY,
    card_id        UUID,
    amount         DECIMAL,
    currency       TEXT,
    status         TEXT,
    merchant       TEXT,
    created_at     TIMESTAMP
);
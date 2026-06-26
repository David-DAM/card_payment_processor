USE payment_processor;
-- Get a card
CREATE TABLE IF NOT EXISTS cards
(
    card_id          UUID PRIMARY KEY,
    cardholder_name  TEXT,
    masked_pan       TEXT,
    credit_limit     DECIMAL,
    available_credit DECIMAL,
    currency         TEXT,
    created_at       TIMESTAMP
);
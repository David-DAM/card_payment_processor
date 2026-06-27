CREATE KEYSPACE IF NOT EXISTS payment_processor
    WITH replication = {
        'class': 'SimpleStrategy',
        'replication_factor': 1
        };

USE payment_processor;

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

CREATE TABLE IF NOT EXISTS payment_events_by_transaction
(
    transaction_id UUID,
    event_time     TIMESTAMP,
    event_type     TEXT,
    amount         DECIMAL,
    currency       TEXT,
    payload        TEXT,

    PRIMARY KEY (
        transaction_id,
        event_time
        )
)
            WITH CLUSTERING ORDER BY (event_time ASC);

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
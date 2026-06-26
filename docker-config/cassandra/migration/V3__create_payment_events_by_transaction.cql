USE payment_processor;
-- Get all the events for a transaction
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
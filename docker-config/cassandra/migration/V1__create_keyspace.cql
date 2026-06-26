CREATE KEYSPACE IF NOT EXISTS payment_processor
    WITH replication = {
        'class': 'SimpleStrategy',
        'replication_factor': 2
        };

USE payment_processor;
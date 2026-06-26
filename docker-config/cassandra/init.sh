#!/bin/bash

set -e

echo "Waiting for Cassandra..."

until cqlsh cassandra-1 9042 -e "DESCRIBE KEYSPACES" >/dev/null 2>&1; do
    sleep 5
done

echo "Cassandra is ready."

for migration in $(find /migration -name "*.cql" | sort); do
    echo "Executing $(basename "$migration")"
    cqlsh cassandra-1 9042 -f "$migration"
done

echo "All migrations executed successfully."
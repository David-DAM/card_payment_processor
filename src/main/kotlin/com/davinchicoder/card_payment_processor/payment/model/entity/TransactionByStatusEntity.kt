package com.davinchicoder.card_payment_processor.payment.model.entity

import org.springframework.data.cassandra.core.cql.Ordering
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.*
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.*

@PrimaryKeyClass
data class TransactionByStatusKey(
    @PrimaryKeyColumn(name = "status", type = PrimaryKeyType.PARTITIONED)
    val status: String,
    @PrimaryKeyColumn(name = "transaction_date", type = PrimaryKeyType.PARTITIONED)
    val transactionDate: LocalDate,
    @PrimaryKeyColumn(name = "created_at", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    val createdAt: Instant,
    @PrimaryKeyColumn(name = "transaction_id", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    val transactionId: UUID
) : Serializable

@Table("transactions_by_status")
data class TransactionByStatusEntity(
    @PrimaryKey
    val key: TransactionByStatusKey,
    @Column("card_id")
    val cardId: UUID,
    val amount: BigDecimal
)

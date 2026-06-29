package com.davinchicoder.card_payment_processor.payment.model.entity

import org.springframework.data.cassandra.core.cql.Ordering
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.*

@PrimaryKeyClass
data class PaymentByCardKey(
    @PrimaryKeyColumn(name = "card_id", type = PrimaryKeyType.PARTITIONED)
    val cardId: UUID,
    @PrimaryKeyColumn(name = "payment_date", type = PrimaryKeyType.PARTITIONED)
    val paymentDate: LocalDate,
    @PrimaryKeyColumn(name = "created_at", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    val createdAt: Instant,
    @PrimaryKeyColumn(name = "transaction_id", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    val transactionId: UUID
) : Serializable

@Table("payments_by_card")
data class PaymentByCardEntity(
    @PrimaryKey
    val key: PaymentByCardKey,
    val amount: BigDecimal,
    val currency: String,
    val status: String,
    val merchant: String
)

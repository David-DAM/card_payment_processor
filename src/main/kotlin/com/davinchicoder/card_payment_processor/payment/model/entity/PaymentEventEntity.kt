package com.davinchicoder.card_payment_processor.payment.model.entity

import org.springframework.data.cassandra.core.cql.Ordering
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.*
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@PrimaryKeyClass
data class PaymentEventKey(
    @PrimaryKeyColumn(name = "transaction_id", type = PrimaryKeyType.PARTITIONED)
    val transactionId: UUID,
    @PrimaryKeyColumn(name = "event_time", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    val eventTime: Instant
) : Serializable

@Table("payment_events_by_transaction")
data class PaymentEventEntity(
    @PrimaryKey
    val key: PaymentEventKey,
    @Column("event_type")
    val eventType: String,
    val amount: BigDecimal,
    val currency: String,
    val payload: String
)

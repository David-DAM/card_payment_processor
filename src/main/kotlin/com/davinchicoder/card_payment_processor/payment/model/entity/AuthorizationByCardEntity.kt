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
data class AuthorizationByCardKey(
    @PrimaryKeyColumn(name = "card_id", type = PrimaryKeyType.PARTITIONED)
    val cardId: UUID,
    @PrimaryKeyColumn(name = "authorization_date", type = PrimaryKeyType.PARTITIONED)
    val authorizationDate: LocalDate,
    @PrimaryKeyColumn(name = "created_at", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    val createdAt: Instant,
    @PrimaryKeyColumn(name = "transaction_id", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    val transactionId: UUID
) : Serializable

@Table("authorizations_by_card")
data class AuthorizationByCardEntity(
    @PrimaryKey
    val key: AuthorizationByCardKey,
    val amount: BigDecimal,
    val merchant: String,
    val status: String
)

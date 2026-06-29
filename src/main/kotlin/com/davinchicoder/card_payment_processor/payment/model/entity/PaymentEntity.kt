package com.davinchicoder.card_payment_processor.payment.model.entity

import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Table("payments")
data class PaymentEntity(
    @PrimaryKey
    @Column("transaction_id")
    val transactionId: UUID,
    @Column("card_id")
    val cardId: UUID,
    val amount: BigDecimal,
    val currency: String,
    val status: String,
    val merchant: String,
    @Column("created_at")
    val createdAt: Instant
)
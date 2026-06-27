package com.davinchicoder.card_payment_processor.card

import org.springframework.data.annotation.Id
import org.springframework.data.cassandra.core.mapping.CassandraType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Table("cards")
data class CardEntity(
    @Id
    @PrimaryKey
    @Column("card_id")
    val cardId: UUID,
    @Column("cardholder_name")
    val cardholderName: String,
    @Column("masked_pan")
    val maskedPan: String,
    @Column("credit_limit")
    val creditLimit: BigDecimal,
    @Column("available_credit")
    val availableCredit: BigDecimal,
    val currency: String,
    @Column("created_at")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    val createdAt: Instant
)

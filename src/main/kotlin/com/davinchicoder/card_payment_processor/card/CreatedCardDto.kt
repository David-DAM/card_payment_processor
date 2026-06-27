package com.davinchicoder.card_payment_processor.card

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class CreatedCardDto(
    val cardId: UUID,
    val holder: String,
    val maskedPan: String,
    val creditLimit: BigDecimal,
    val availableCredit: BigDecimal,
    val currency: String,
    val createdAt: Instant
)

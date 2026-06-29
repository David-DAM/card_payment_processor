package com.davinchicoder.card_payment_processor.card.dto

import java.math.BigDecimal

data class CardDto(

    val holder: String,
    val pan: String,
    val creditLimit: BigDecimal,
    val availableCredit: BigDecimal,
    val currency: String,

    )

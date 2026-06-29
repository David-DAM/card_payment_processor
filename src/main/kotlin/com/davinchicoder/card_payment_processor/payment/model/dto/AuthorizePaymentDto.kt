package com.davinchicoder.card_payment_processor.payment.model.dto

import java.math.BigDecimal
import java.util.*

data class AuthorizePaymentDto(
    val cardId: UUID,
    val amount: BigDecimal,
    val currency: String,
    val merchant: String
)

data class AuthorizedPaymentDto(
    val transactionId: UUID,
    val status: String
)

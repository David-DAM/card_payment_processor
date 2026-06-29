package com.davinchicoder.card_payment_processor.payment.model.events

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class PaymentEventMetadata(
    val transactionId: UUID,
    val cardId: UUID,
    val amount: BigDecimal,
    val currency: String,
    val merchant: String,
    val occurredAt: Instant
) : PaymentEvent

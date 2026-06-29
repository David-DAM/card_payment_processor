package com.davinchicoder.card_payment_processor.payment.model.events

import java.math.BigDecimal
import java.util.*

data class ReversalCompleted(
    val metadata: PaymentEventMetadata,
    val reversalId: UUID,
    val reversedAmount: BigDecimal,
    val reason: ReversalReason,
    val availableBalanceAfter: BigDecimal
) : PaymentEvent

enum class ReversalReason {
    USER_CANCELLED,
    EXPIRED_AUTHORIZATION,
    MERCHANT_CANCELLED
}

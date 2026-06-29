package com.davinchicoder.card_payment_processor.payment.model.events

import java.math.BigDecimal
import java.util.*

data class RefundCompleted(
    val metadata: PaymentEventMetadata,
    val refundId: UUID,
    val refundedAmount: BigDecimal,
    val reason: RefundReason,
    val remainingCapturedAmount: BigDecimal
) : PaymentEvent

enum class RefundReason {
    CUSTOMER_REQUEST,
    FRAUD,
    MERCHANT_ERROR
}

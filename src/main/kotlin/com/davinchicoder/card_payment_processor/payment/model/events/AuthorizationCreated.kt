package com.davinchicoder.card_payment_processor.payment.model.events

import java.math.BigDecimal
import java.util.*

data class AuthorizationCreated(
    val metadata: PaymentEventMetadata,
    val authorizationId: UUID,
    val approvedAmount: BigDecimal,
    val availableBalanceAfter: BigDecimal,
    val status: AuthorizationStatus = AuthorizationStatus.APPROVED
) : PaymentEvent

enum class AuthorizationStatus {
    APPROVED,
    DECLINED
}

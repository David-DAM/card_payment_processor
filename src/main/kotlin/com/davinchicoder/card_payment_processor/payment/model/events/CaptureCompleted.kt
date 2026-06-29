package com.davinchicoder.card_payment_processor.payment.model.events

import java.math.BigDecimal
import java.util.*

data class CaptureCompleted(
    val metadata: PaymentEventMetadata,
    val captureId: UUID,
    val capturedAmount: BigDecimal,
    val remainingAuthorizedAmount: BigDecimal
) : PaymentEvent

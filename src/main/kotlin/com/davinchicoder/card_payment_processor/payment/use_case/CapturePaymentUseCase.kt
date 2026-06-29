package com.davinchicoder.card_payment_processor.payment.use_case

import com.davinchicoder.card_payment_processor.common.UseCase
import com.davinchicoder.card_payment_processor.payment.model.PaymentStatus
import com.davinchicoder.card_payment_processor.payment.model.entity.*
import com.davinchicoder.card_payment_processor.payment.model.events.CaptureCompleted
import com.davinchicoder.card_payment_processor.payment.model.events.PaymentEventMetadata
import com.davinchicoder.card_payment_processor.payment.repository.PaymentByCardRepository
import com.davinchicoder.card_payment_processor.payment.repository.PaymentEventRepository
import com.davinchicoder.card_payment_processor.payment.repository.PaymentRepository
import com.davinchicoder.card_payment_processor.payment.repository.TransactionByStatusRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

@Service
class CapturePaymentUseCase(
    private val paymentRepository: PaymentRepository,
    private val paymentEventRepository: PaymentEventRepository,
    private val paymentByCardRepository: PaymentByCardRepository,
    private val transactionByStatusRepository: TransactionByStatusRepository,
    private val eventPublisher: ApplicationEventPublisher
) : UseCase<UUID, Unit> {

    override suspend fun execute(input: UUID) {
        val payment = paymentRepository.findById(input) ?: throw RuntimeException("Payment not found")
        val now = Instant.now()
        val today = now.atZone(ZoneOffset.UTC).toLocalDate()

        val updatedPayment = payment.copy(status = PaymentStatus.CAPTURED.name)

        val paymentByCard = PaymentByCardEntity(
            key = PaymentByCardKey(
                cardId = payment.cardId,
                paymentDate = today,
                createdAt = now,
                transactionId = payment.transactionId
            ),
            amount = payment.amount,
            currency = payment.currency,
            status = PaymentStatus.CAPTURED.name,
            merchant = payment.merchant
        )

        val transactionByStatus = TransactionByStatusEntity(
            key = TransactionByStatusKey(
                status = PaymentStatus.CAPTURED.name,
                transactionDate = today,
                createdAt = now,
                transactionId = payment.transactionId
            ),
            cardId = payment.cardId,
            amount = payment.amount
        )

        val paymentEvent = PaymentEventEntity(
            key = PaymentEventKey(
                transactionId = payment.transactionId,
                eventTime = now
            ),
            eventType = "CAPTURE_COMPLETED",
            amount = payment.amount,
            currency = payment.currency,
            payload = "{}"
        )

        paymentRepository.save(updatedPayment)
        paymentByCardRepository.save(paymentByCard)
        transactionByStatusRepository.save(transactionByStatus)
        paymentEventRepository.save(paymentEvent)

        val event = CaptureCompleted(
            metadata = PaymentEventMetadata(
                transactionId = input,
                cardId = payment.cardId,
                amount = payment.amount,
                currency = payment.currency,
                merchant = payment.merchant,
                occurredAt = now
            ),
            captureId = UUID.randomUUID(),
            capturedAmount = payment.amount,
            remainingAuthorizedAmount = BigDecimal.ZERO
        )

        eventPublisher.publishEvent(event)
    }
}

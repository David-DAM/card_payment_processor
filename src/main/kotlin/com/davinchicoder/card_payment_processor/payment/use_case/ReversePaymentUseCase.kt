package com.davinchicoder.card_payment_processor.payment.use_case

import com.davinchicoder.card_payment_processor.common.UseCase
import com.davinchicoder.card_payment_processor.payment.model.PaymentStatus
import com.davinchicoder.card_payment_processor.payment.model.entity.*
import com.davinchicoder.card_payment_processor.payment.model.events.PaymentEventMetadata
import com.davinchicoder.card_payment_processor.payment.model.events.ReversalCompleted
import com.davinchicoder.card_payment_processor.payment.model.events.ReversalReason
import com.davinchicoder.card_payment_processor.payment.repository.PaymentByCardRepository
import com.davinchicoder.card_payment_processor.payment.repository.PaymentEventRepository
import com.davinchicoder.card_payment_processor.payment.repository.PaymentRepository
import com.davinchicoder.card_payment_processor.payment.repository.TransactionByStatusRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

@Service
class ReversePaymentUseCase(
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

        val updatedPayment = payment.copy(status = PaymentStatus.REVERSED.name)

        val paymentByCard = PaymentByCardEntity(
            key = PaymentByCardKey(
                cardId = payment.cardId,
                paymentDate = today,
                createdAt = now,
                transactionId = payment.transactionId
            ),
            amount = payment.amount,
            currency = payment.currency,
            status = PaymentStatus.REVERSED.name,
            merchant = payment.merchant
        )

        val transactionByStatus = TransactionByStatusEntity(
            key = TransactionByStatusKey(
                status = PaymentStatus.REVERSED.name,
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
            eventType = "REVERSAL_COMPLETED",
            amount = payment.amount,
            currency = payment.currency,
            payload = "{}"
        )

        paymentRepository.save(updatedPayment)
        paymentByCardRepository.save(paymentByCard)
        transactionByStatusRepository.save(transactionByStatus)
        paymentEventRepository.save(paymentEvent)

        val event = ReversalCompleted(
            metadata = PaymentEventMetadata(
                transactionId = input,
                cardId = payment.cardId,
                amount = payment.amount,
                currency = payment.currency,
                merchant = payment.merchant,
                occurredAt = now
            ),
            reversalId = UUID.randomUUID(),
            reversedAmount = payment.amount,
            reason = ReversalReason.MERCHANT_CANCELLED,
            availableBalanceAfter = payment.amount
        )

        eventPublisher.publishEvent(event)
    }
}

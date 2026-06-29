package com.davinchicoder.card_payment_processor.payment.use_case

import com.davinchicoder.card_payment_processor.common.UseCase
import com.davinchicoder.card_payment_processor.payment.model.PaymentStatus
import com.davinchicoder.card_payment_processor.payment.model.dto.AuthorizePaymentDto
import com.davinchicoder.card_payment_processor.payment.model.dto.AuthorizedPaymentDto
import com.davinchicoder.card_payment_processor.payment.model.entity.*
import com.davinchicoder.card_payment_processor.payment.model.events.AuthorizationCreated
import com.davinchicoder.card_payment_processor.payment.model.events.PaymentEventMetadata
import com.davinchicoder.card_payment_processor.payment.repository.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

@Service
class AuthorizePaymentUseCase(
    private val paymentRepository: PaymentRepository,
    private val paymentEventRepository: PaymentEventRepository,
    private val paymentByCardRepository: PaymentByCardRepository,
    private val authorizationByCardRepository: AuthorizationByCardRepository,
    private val transactionByStatusRepository: TransactionByStatusRepository,
    private val eventPublisher: ApplicationEventPublisher
) : UseCase<AuthorizePaymentDto, AuthorizedPaymentDto> {

    override suspend fun execute(input: AuthorizePaymentDto): AuthorizedPaymentDto {
        val transactionId = UUID.randomUUID()
        val now = Instant.now()
        val today = now.atZone(ZoneOffset.UTC).toLocalDate()

        val payment = PaymentEntity(
            transactionId = transactionId,
            cardId = input.cardId,
            amount = input.amount,
            currency = input.currency,
            status = PaymentStatus.AUTHORIZED.name,
            merchant = input.merchant,
            createdAt = now
        )

        val paymentByCard = PaymentByCardEntity(
            key = PaymentByCardKey(
                cardId = input.cardId,
                paymentDate = today,
                createdAt = now,
                transactionId = transactionId
            ),
            amount = input.amount,
            currency = input.currency,
            status = PaymentStatus.AUTHORIZED.name,
            merchant = input.merchant
        )

        val authorizationByCard = AuthorizationByCardEntity(
            key = AuthorizationByCardKey(
                cardId = input.cardId,
                authorizationDate = today,
                createdAt = now,
                transactionId = transactionId
            ),
            amount = input.amount,
            merchant = input.merchant,
            status = PaymentStatus.AUTHORIZED.name
        )

        val transactionByStatus = TransactionByStatusEntity(
            key = TransactionByStatusKey(
                status = PaymentStatus.AUTHORIZED.name,
                transactionDate = today,
                createdAt = now,
                transactionId = transactionId
            ),
            cardId = input.cardId,
            amount = input.amount
        )

        val paymentEvent = PaymentEventEntity(
            key = PaymentEventKey(
                transactionId = transactionId,
                eventTime = now
            ),
            eventType = "AUTHORIZATION_CREATED",
            amount = input.amount,
            currency = input.currency,
            payload = "{}" // Simple payload for now
        )

        // Save all entities
        paymentRepository.save(payment)
        paymentByCardRepository.save(paymentByCard)
        authorizationByCardRepository.save(authorizationByCard)
        transactionByStatusRepository.save(transactionByStatus)
        paymentEventRepository.save(paymentEvent)

        val event = AuthorizationCreated(
            metadata = PaymentEventMetadata(
                transactionId = transactionId,
                cardId = input.cardId,
                amount = input.amount,
                currency = input.currency,
                merchant = input.merchant,
                occurredAt = now
            ),
            authorizationId = UUID.randomUUID(),
            approvedAmount = input.amount,
            availableBalanceAfter = input.amount // Simplification for now
        )

        eventPublisher.publishEvent(event)

        return AuthorizedPaymentDto(
            transactionId = transactionId,
            status = "APPROVED"
        )
    }
}

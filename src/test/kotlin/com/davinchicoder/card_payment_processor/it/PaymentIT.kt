package com.davinchicoder.card_payment_processor.it

import com.davinchicoder.card_payment_processor.card.entity.CardEntity
import com.davinchicoder.card_payment_processor.card.repository.CardQueryRepository
import com.davinchicoder.card_payment_processor.payment.model.PaymentStatus
import com.davinchicoder.card_payment_processor.payment.model.dto.AuthorizePaymentDto
import com.davinchicoder.card_payment_processor.payment.model.dto.AuthorizedPaymentDto
import com.davinchicoder.card_payment_processor.payment.repository.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Import(TestcontainersConfiguration::class)
@AutoConfigureWebTestClient
@SpringBootTest
class PaymentIT {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var cardRepository: CardQueryRepository

    @Autowired
    lateinit var paymentRepository: PaymentRepository

    @Autowired
    lateinit var paymentEventRepository: PaymentEventRepository

    @Autowired
    lateinit var paymentByCardRepository: PaymentByCardRepository

    @Autowired
    lateinit var authorizationByCardRepository: AuthorizationByCardRepository

    @Autowired
    lateinit var transactionByStatusRepository: TransactionByStatusRepository

    @Test
    fun `should process a full payment lifecycle`() = runBlocking {
        // 1. Setup: Create a card
        val cardId = UUID.randomUUID()
        val card = CardEntity(
            cardId = cardId,
            cardholderName = "John Doe",
            maskedPan = "************9012",
            creditLimit = BigDecimal("5000.00"),
            availableCredit = BigDecimal("5000.00"),
            currency = "USD",
            createdAt = Instant.now()
        )
        cardRepository.save(card)

        // 2. Authorize Payment
        val authorizeDto = AuthorizePaymentDto(
            cardId = cardId,
            amount = BigDecimal("100.00"),
            currency = "USD",
            merchant = "Test Merchant"
        )

        val authorizedPayment = webTestClient.post()
            .uri("/payments/authorize")
            .bodyValue(authorizeDto)
            .exchange()
            .expectStatus().isOk
            .expectBody<AuthorizedPaymentDto>()
            .returnResult()
            .responseBody!!

        assertNotNull(authorizedPayment.transactionId)
        assertEquals("APPROVED", authorizedPayment.status)

        // Verify Authorize in DB
        val payment = paymentRepository.findById(authorizedPayment.transactionId)
        assertNotNull(payment)
        assertEquals(PaymentStatus.AUTHORIZED.name, payment?.status)
        assertEquals(authorizeDto.amount, payment?.amount)

        // 3. Capture Payment
        webTestClient.post()
            .uri("/payments/${authorizedPayment.transactionId}/capture")
            .exchange()
            .expectStatus().isOk

        // Verify Capture in DB
        val capturedPayment = paymentRepository.findById(authorizedPayment.transactionId)
        assertEquals(PaymentStatus.CAPTURED.name, capturedPayment?.status)

        // 4. Refund Payment
        webTestClient.post()
            .uri("/payments/${authorizedPayment.transactionId}/refund")
            .exchange()
            .expectStatus().isOk

        // Verify Refund in DB
        val refundedPayment = paymentRepository.findById(authorizedPayment.transactionId)
        assertEquals(PaymentStatus.REFUNDED.name, refundedPayment?.status)

        // 5. Reverse Payment (Starting a new authorization for reversal)
        val authorizeDto2 = AuthorizePaymentDto(
            cardId = cardId,
            amount = BigDecimal("50.00"),
            currency = "USD",
            merchant = "Another Merchant"
        )

        val authorizedPayment2 = webTestClient.post()
            .uri("/payments/authorize")
            .bodyValue(authorizeDto2)
            .exchange()
            .expectStatus().isOk
            .expectBody<AuthorizedPaymentDto>()
            .returnResult()
            .responseBody!!

        webTestClient.post()
            .uri("/payments/${authorizedPayment2.transactionId}/reverse")
            .exchange()
            .expectStatus().isOk

        // Verify Reversal in DB
        val reversedPayment = paymentRepository.findById(authorizedPayment2.transactionId)
        assertEquals(PaymentStatus.REVERSED.name, reversedPayment?.status)

        // Verify Events were recorded
        val events = paymentEventRepository.findAll().toList()
        assertTrue(events.any { it.key.transactionId == authorizedPayment.transactionId && it.eventType == "AUTHORIZATION_CREATED" })
        assertTrue(events.any { it.key.transactionId == authorizedPayment.transactionId && it.eventType == "CAPTURE_COMPLETED" })
        assertTrue(events.any { it.key.transactionId == authorizedPayment.transactionId && it.eventType == "REFUND_COMPLETED" })
        assertTrue(events.any { it.key.transactionId == authorizedPayment2.transactionId && it.eventType == "REVERSAL_COMPLETED" })
    }
}

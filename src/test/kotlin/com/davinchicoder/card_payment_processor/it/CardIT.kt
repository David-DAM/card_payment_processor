package com.davinchicoder.card_payment_processor.it

import com.davinchicoder.card_payment_processor.card.CardDto
import com.davinchicoder.card_payment_processor.card.CardQueryRepository
import com.davinchicoder.card_payment_processor.card.CreatedCardDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.math.BigDecimal

@Import(TestcontainersConfiguration::class)
@AutoConfigureWebTestClient
@SpringBootTest
class CardIT {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var repository: CardQueryRepository

    @Test
    suspend fun `should create a card`() {

        val cardDto = CardDto(
            holder = "John Doe",
            pan = "4532123456789012",
            creditLimit = BigDecimal(5000.00),
            availableCredit = BigDecimal(5000.00),
            currency = "USD"
        )

        val createdCard = webTestClient.post()
            .uri("/cards")
            .bodyValue(cardDto)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody<CreatedCardDto>()
            .returnResult()
            .responseBody!!

        assert(createdCard.holder == cardDto.holder)
        assert(createdCard.maskedPan == "************9012")
        assert(createdCard.creditLimit == cardDto.creditLimit)
        assert(createdCard.availableCredit == cardDto.availableCredit)
        assert(createdCard.currency == cardDto.currency)

        val cardEntity = repository.findById(createdCard.cardId)
        assert(cardEntity != null)
        assert(cardEntity?.cardholderName == cardDto.holder)
        assert(cardEntity?.maskedPan == "************9012")
        assert(cardEntity?.creditLimit == cardDto.creditLimit)
        assert(cardEntity?.availableCredit == cardDto.availableCredit)
        assert(cardEntity?.currency == cardDto.currency)
        assert(cardEntity?.createdAt != null)
    }

}
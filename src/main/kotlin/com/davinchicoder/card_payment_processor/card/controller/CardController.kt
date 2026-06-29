package com.davinchicoder.card_payment_processor.card.controller

import com.davinchicoder.card_payment_processor.card.dto.CardDto
import com.davinchicoder.card_payment_processor.card.dto.CreatedCardDto
import com.davinchicoder.card_payment_processor.card.use_case.CreateCardUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cards")
class CardController(
    private val createCardUseCase: CreateCardUseCase
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun card(@RequestBody card: CardDto): CreatedCardDto {
        return createCardUseCase.execute(card)
    }

}
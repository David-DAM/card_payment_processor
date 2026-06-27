package com.davinchicoder.card_payment_processor.card

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
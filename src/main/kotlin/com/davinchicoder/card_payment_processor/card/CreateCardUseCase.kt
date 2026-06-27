package com.davinchicoder.card_payment_processor.card

import com.davinchicoder.card_payment_processor.common.UseCase
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class CreateCardUseCase(
    private val cardQueryRepository: CardQueryRepository
) : UseCase<CardDto, CreatedCardDto> {

    override suspend fun execute(input: CardDto): CreatedCardDto {

        val entity = CardEntity(
            cardId = UUID.randomUUID(),
            cardholderName = input.holder,
            maskedPan = input.pan.toMaskedPan(),
            creditLimit = input.creditLimit,
            availableCredit = input.availableCredit,
            currency = input.currency,
            createdAt = Instant.now()
        )

        val saved = cardQueryRepository.save(entity)

        return CreatedCardDto(
            cardId = saved.cardId,
            holder = saved.cardholderName,
            maskedPan = saved.maskedPan,
            creditLimit = saved.creditLimit,
            availableCredit = saved.availableCredit,
            currency = saved.currency,
            createdAt = saved.createdAt
        )
    }
}
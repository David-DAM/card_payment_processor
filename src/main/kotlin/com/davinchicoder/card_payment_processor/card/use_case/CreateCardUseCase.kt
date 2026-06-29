package com.davinchicoder.card_payment_processor.card.use_case

import com.davinchicoder.card_payment_processor.card.dto.CardDto
import com.davinchicoder.card_payment_processor.card.dto.CreatedCardDto
import com.davinchicoder.card_payment_processor.card.entity.CardEntity
import com.davinchicoder.card_payment_processor.card.repository.CardQueryRepository
import com.davinchicoder.card_payment_processor.card.utils.toMaskedPan
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
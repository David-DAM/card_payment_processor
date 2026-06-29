package com.davinchicoder.card_payment_processor.card.repository

import com.davinchicoder.card_payment_processor.card.entity.CardEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface CardQueryRepository : CoroutineCrudRepository<CardEntity, UUID>
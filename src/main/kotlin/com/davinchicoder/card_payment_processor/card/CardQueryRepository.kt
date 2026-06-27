package com.davinchicoder.card_payment_processor.card

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface CardQueryRepository : CoroutineCrudRepository<CardEntity, UUID>
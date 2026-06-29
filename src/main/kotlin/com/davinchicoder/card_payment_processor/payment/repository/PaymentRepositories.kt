package com.davinchicoder.card_payment_processor.payment.repository

import com.davinchicoder.card_payment_processor.payment.model.entity.*
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface PaymentRepository : CoroutineCrudRepository<PaymentEntity, UUID>

interface PaymentEventRepository : CoroutineCrudRepository<PaymentEventEntity, PaymentEventKey>

interface PaymentByCardRepository : CoroutineCrudRepository<PaymentByCardEntity, PaymentByCardKey>

interface AuthorizationByCardRepository : CoroutineCrudRepository<AuthorizationByCardEntity, AuthorizationByCardKey>

interface TransactionByStatusRepository : CoroutineCrudRepository<TransactionByStatusEntity, TransactionByStatusKey>

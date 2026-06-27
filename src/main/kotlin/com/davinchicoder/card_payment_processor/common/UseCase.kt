package com.davinchicoder.card_payment_processor.common

interface UseCase<I, O> {

    suspend fun execute(input: I): O
}
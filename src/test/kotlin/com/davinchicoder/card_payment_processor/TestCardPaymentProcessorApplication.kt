package com.davinchicoder.card_payment_processor

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<CardPaymentProcessorApplication>().with(TestcontainersConfiguration::class).run(*args)
}

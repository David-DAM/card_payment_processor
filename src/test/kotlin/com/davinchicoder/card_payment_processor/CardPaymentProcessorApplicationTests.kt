package com.davinchicoder.card_payment_processor

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class CardPaymentProcessorApplicationTests {

    @Test
    fun contextLoads() {
    }

}

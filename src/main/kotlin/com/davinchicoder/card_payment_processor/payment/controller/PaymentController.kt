package com.davinchicoder.card_payment_processor.payment.controller

import com.davinchicoder.card_payment_processor.payment.model.dto.AuthorizePaymentDto
import com.davinchicoder.card_payment_processor.payment.model.dto.AuthorizedPaymentDto
import com.davinchicoder.card_payment_processor.payment.use_case.AuthorizePaymentUseCase
import com.davinchicoder.card_payment_processor.payment.use_case.CapturePaymentUseCase
import com.davinchicoder.card_payment_processor.payment.use_case.RefundPaymentUseCase
import com.davinchicoder.card_payment_processor.payment.use_case.ReversePaymentUseCase
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/payments")
class PaymentController(
    private val authorizePaymentUseCase: AuthorizePaymentUseCase,
    private val capturePaymentUseCase: CapturePaymentUseCase,
    private val reversePaymentUseCase: ReversePaymentUseCase,
    private val refundPaymentUseCase: RefundPaymentUseCase
) {

    @PostMapping("/authorize")
    suspend fun authorize(@RequestBody authorizePaymentDto: AuthorizePaymentDto): AuthorizedPaymentDto {
        return authorizePaymentUseCase.execute(authorizePaymentDto)
    }

    @PostMapping("/{id}/capture")
    suspend fun capture(@PathVariable id: UUID) {
        capturePaymentUseCase.execute(id)
    }

    @PostMapping("/{id}/reverse")
    suspend fun reverse(@PathVariable id: UUID) {
        reversePaymentUseCase.execute(id)
    }

    @PostMapping("/{id}/refund")
    suspend fun refund(@PathVariable id: UUID) {
        refundPaymentUseCase.execute(id)
    }

}
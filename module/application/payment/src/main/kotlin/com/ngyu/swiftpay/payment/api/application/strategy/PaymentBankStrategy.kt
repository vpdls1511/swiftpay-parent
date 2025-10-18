package com.ngyu.swiftpay.payment.api.application.strategy

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.payment.api.application.service.payment.PaymentBankService
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto
import org.springframework.stereotype.Component

@Component
class PaymentBankStrategy(
  private val bankService: PaymentBankService
): PaymentStrategy() {
  override fun shouldAsyncProcessing(payment: Payment): Boolean {
//    TODO("Not yet implemented")
    return false
  }

  override suspend fun process(payment: Payment): PaymentResponseDto {
    TODO("Not yet implemented")
  }

  override fun getStrategyName(): String {
    return "계좌이체"
  }
}

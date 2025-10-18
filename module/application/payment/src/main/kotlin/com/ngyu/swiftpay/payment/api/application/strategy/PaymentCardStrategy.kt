package com.ngyu.swiftpay.payment.api.application.strategy

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.payment.api.application.service.payment.PaymentCardService
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto
import org.springframework.stereotype.Component

@Component
class PaymentCardStrategy(
  private val cardService: PaymentCardService,
): PaymentStrategy() {
  override fun shouldAsyncProcessing(payment: Payment): Boolean {
//    TODO("Not yet implemented")
    return true
  }

  override suspend fun process(payment: Payment): PaymentResponseDto {
    TODO("Not yet implemented")
  }

  override fun getStrategyName(): String {
    return "카드결제"
  }
}

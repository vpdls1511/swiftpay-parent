package com.ngyu.swiftpay.payment.application.strategy

import com.ngyu.swiftpay.core.domain.money.Money
import com.ngyu.swiftpay.core.domain.payment.model.Payment
import com.ngyu.swiftpay.payment.application.service.payment.PaymentCardService
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PaymentCardStrategy(
  private val cardService: PaymentCardService,
): PaymentStrategy() {
  override fun shouldAsyncProcessing(payment: Payment): Boolean {
    return payment.amount >= Money(BigDecimal.valueOf(10000000), payment.amount.currency)
  }

  override suspend fun process(payment: Payment): PaymentResponseDto {
    TODO("Not yet implemented")
  }

  override fun getStrategyName(): String {
    return "카드결제"
  }
}

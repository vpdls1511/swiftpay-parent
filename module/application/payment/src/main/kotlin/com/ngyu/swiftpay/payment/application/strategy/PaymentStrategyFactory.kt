package com.ngyu.swiftpay.payment.application.strategy

import com.ngyu.swiftpay.core.domain.payment.model.Payment
import com.ngyu.swiftpay.core.domain.payment.model.PaymentMethod
import org.springframework.stereotype.Component

@Component
class PaymentStrategyFactory(
  private val cardStrategy: PaymentCardStrategy,
  private val bankStrategy: PaymentBankStrategy
) {
  fun getStrategy(payment: Payment): PaymentStrategy {
    return when (payment.method) {
      PaymentMethod.CARD -> cardStrategy
      PaymentMethod.BANK_TRANSFER -> bankStrategy
      else -> throw UnsupportedOperationException("지원하지 않는 결제수단 입니다.")
    }
  }
}

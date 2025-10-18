package com.ngyu.swiftpay.payment.application.strategy

import com.ngyu.swiftpay.core.domain.payment.model.PayMethod
import com.ngyu.swiftpay.core.domain.payment.model.Payment
import org.springframework.stereotype.Component

@Component
class PaymentStrategyFactory(
  private val cardStrategy: PaymentCardStrategy,
  private val bankStrategy: PaymentBankStrategy
) {
  fun getStrategy(payment: Payment): PaymentStrategy {
    return when (payment.method) {
      PayMethod.CARD -> cardStrategy
      PayMethod.BANK_TRANSFER -> bankStrategy
      else -> throw UnsupportedOperationException("지원하지 않는 결제수단 입니다.")
    }
  }
}

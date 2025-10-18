package com.ngyu.swiftpay.payment.application.strategy

import com.ngyu.swiftpay.core.domain.payment.model.Payment
import org.springframework.stereotype.Component

@Component
class PaymentStrategyFactory(
  strategies: List<PaymentStrategy>
) {

  private val strategiesMap = strategies.associateBy { it.getPaymentMethod() }

  fun getStrategy(payment: Payment): PaymentStrategy {
    return strategiesMap[payment.method]
      ?: throw UnsupportedOperationException("지원하지 않는 결제수단 입니다.")
  }
}

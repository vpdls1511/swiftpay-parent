package com.ngyu.swiftpay.infrastructure.client.mock

import com.ngyu.swiftpay.core.domain.payment.model.Payment
import com.ngyu.swiftpay.core.domain.payment.port.CardApiClientPort
import org.springframework.stereotype.Component

@Component
class MockExternalCardApiClient: CardApiClientPort {
  override fun shouldCanPayment(domain: Payment): Boolean {
    return true
  }

  override fun checkLimit(domain: Payment): Boolean {
    return true
  }
}

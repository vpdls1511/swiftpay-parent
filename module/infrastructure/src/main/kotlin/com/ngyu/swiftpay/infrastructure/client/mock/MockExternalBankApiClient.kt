package com.ngyu.swiftpay.infrastructure.client.mock

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.port.BankApiClientPort
import org.springframework.stereotype.Component

@Component
class MockExternalBankApiClient: BankApiClientPort {
  override fun shouldCanPayment(domain: Payment): Boolean {
    return true
  }

  override fun checkBalance(domain: Payment): Boolean {
    return true
  }
}

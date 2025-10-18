package com.ngyu.swiftpay.infrastructure.client.mock

import com.ngyu.swiftpay.core.domain.payment.model.Payment
import com.ngyu.swiftpay.core.domain.payment.port.BankApiClientPort

class MockExternalBankApiClient: BankApiClientPort {
  override fun shouldCanPayment(domain: Payment): Boolean {
    return true
  }

  override fun checkBalance(domain: Payment): Boolean {
    return true
  }
}

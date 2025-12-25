package com.ngyu.swiftpay.core.port.client

import com.ngyu.swiftpay.core.domain.payment.Payment

interface BankApiClientFactory {
  fun getClient(domain: Payment): BankApiClientPort
}

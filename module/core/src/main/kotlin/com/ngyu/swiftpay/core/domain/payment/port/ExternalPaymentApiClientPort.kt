package com.ngyu.swiftpay.core.domain.payment.port

import com.ngyu.swiftpay.core.domain.payment.model.Payment

interface ExternalPaymentApiClientPort {
  fun shouldCanPayment(domain: Payment): Boolean
}


interface CardApiClientPort: ExternalPaymentApiClientPort {
  fun checkLimit(domain: Payment): Boolean
}
interface BankApiClientPort: ExternalPaymentApiClientPort {
  fun checkBalance(domain: Payment): Boolean
}

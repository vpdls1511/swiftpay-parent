package com.ngyu.swiftpay.core.port

import com.ngyu.swiftpay.core.domain.payment.Payment

interface ExternalPaymentApiClientPort {
  fun shouldCanPayment(domain: Payment): Boolean
}


interface CardApiClientPort: ExternalPaymentApiClientPort {
  fun checkLimit(domain: Payment): Boolean
}
interface BankApiClientPort: ExternalPaymentApiClientPort {
  fun checkBalance(domain: Payment): Boolean
}

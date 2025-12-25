package com.ngyu.swiftpay.core.port.client

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.port.client.dto.BankTransferResult
import com.ngyu.swiftpay.core.port.client.dto.CardApprovalResult

interface ExternalPaymentApiClientPort {
  fun shouldCanPayment(domain: Payment): Boolean
}

interface CardApiClientPort: ExternalPaymentApiClientPort {
  fun approve(domain: Payment): CardApprovalResult
}

interface BankApiClientPort: ExternalPaymentApiClientPort {
  fun transfer(domain: Payment): BankTransferResult
}

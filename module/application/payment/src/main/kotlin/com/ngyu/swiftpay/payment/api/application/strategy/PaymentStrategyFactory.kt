package com.ngyu.swiftpay.payment.api.application.strategy

import com.ngyu.swiftpay.core.domain.payment.PayMethod
import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.payment.api.application.service.payment.PaymentBankService
import com.ngyu.swiftpay.payment.api.application.service.payment.PaymentCardService
import org.springframework.stereotype.Component

@Component
class PaymentStrategyFactory(
  private val cardService: PaymentCardService,
  private val bankService: PaymentBankService
) {
  fun getStrategy(payment: Payment): PaymentStrategy {
    return when (payment.method) {
      PayMethod.CARD -> PaymentCardStrategy(cardService)
      PayMethod.BANK_TRANSFER -> PaymentBankStrategy(bankService)
      else -> throw UnsupportedOperationException("지원하지 않는 결제수단 입니다.")
    }
  }
}

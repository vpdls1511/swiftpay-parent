package com.ngyu.swiftpay.payment.application.strategy

import com.ngyu.swiftpay.core.domain.payment.model.PayMethod
import com.ngyu.swiftpay.core.domain.payment.model.Payment
import com.ngyu.swiftpay.payment.application.service.payment.PaymentBankService
import com.ngyu.swiftpay.payment.application.service.payment.PaymentCardService
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

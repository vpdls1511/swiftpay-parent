package com.ngyu.swiftpay.payment.application.strategy

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.PaymentMethod
import com.ngyu.swiftpay.core.port.client.BankApiClientPort
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto
import org.springframework.stereotype.Component

@Component
class PaymentBankStrategy(
  private val bankApiClient: BankApiClientPort
): PaymentStrategy() {
  override fun getPaymentMethod() = PaymentMethod.BANK_TRANSFER

  override fun shouldAsyncProcessing(payment: Payment): Boolean {
//    TODO("Not yet implemented")
    return false
  }

  override fun process(payment: Payment): PaymentResponseDto {
    TODO("Not yet implemented")
  }

  override fun getStrategyName(): String {
    return "계좌이체"
  }
}

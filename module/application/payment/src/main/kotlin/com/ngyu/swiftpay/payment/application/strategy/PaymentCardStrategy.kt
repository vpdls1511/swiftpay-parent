package com.ngyu.swiftpay.payment.application.strategy

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.PaymentMethod
import com.ngyu.swiftpay.core.port.client.BankApiClientPort
import com.ngyu.swiftpay.core.vo.Money
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PaymentCardStrategy(
  private val cardApiClient: BankApiClientPort
): PaymentStrategy() {
  override fun getPaymentMethod() = PaymentMethod.CARD

  override fun shouldAsyncProcessing(payment: Payment): Boolean {
    return payment.amount >= Money(BigDecimal.valueOf(10000000), payment.amount.currency)
  }

  override fun process(payment: Payment): PaymentResponseDto {
    TODO("Not yet implemented")
  }

  override fun getStrategyName(): String {
    return "카드결제"
  }
}

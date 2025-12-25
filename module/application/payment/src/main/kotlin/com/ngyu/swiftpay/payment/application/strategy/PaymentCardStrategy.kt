package com.ngyu.swiftpay.payment.application.strategy

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.PaymentMethod
import com.ngyu.swiftpay.core.port.client.CardApiClientFactory
import com.ngyu.swiftpay.core.port.client.CardApiClientPort
import org.springframework.stereotype.Component

@Component
class PaymentCardStrategy(
  private val cardApiClientFactory: CardApiClientFactory,
  private val cardApiClient: CardApiClientPort
): PaymentStrategy() {
  override fun getPaymentMethod() = PaymentMethod.CARD

  override fun shouldAsyncProcessing(payment: Payment): Boolean = false

  override fun process(payment: Payment): Payment {
    val inProgress = payment.inProgress()
    val cardApiClient = cardApiClientFactory.getClient(inProgress)
    val result = cardApiClient.approve(inProgress)

    return when {
      result.isSuccess -> inProgress.success()
      else -> inProgress.failed(result.message)
    }
  }

  override fun getStrategyName(): String {
    return "카드결제"
  }
}

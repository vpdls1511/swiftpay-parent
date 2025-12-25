package com.ngyu.swiftpay.payment.application.strategy

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.PaymentMethod

/**
 * 결제 전략 인터페이스
 */
sealed class PaymentStrategy {
  abstract fun getPaymentMethod(): PaymentMethod
  abstract fun shouldAsyncProcessing(payment: Payment): Boolean
  abstract fun process(payment: Payment): Payment
  abstract fun getStrategyName(): String
}

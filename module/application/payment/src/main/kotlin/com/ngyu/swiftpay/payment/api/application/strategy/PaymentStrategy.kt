package com.ngyu.swiftpay.payment.api.application.strategy

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto

/**
 * 결제 전략 인터페이스
 */
sealed class PaymentStrategy {

  abstract fun shouldAsyncProcessing(payment: Payment): Boolean
  abstract suspend fun process(payment: Payment): PaymentResponseDto
  abstract fun getStrategyName(): String

}

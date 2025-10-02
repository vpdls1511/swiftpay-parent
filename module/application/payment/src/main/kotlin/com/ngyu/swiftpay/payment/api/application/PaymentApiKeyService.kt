package com.ngyu.swiftpay.payment.api.application

import com.ngyu.swiftpay.payment.api.dto.ApiKeyResponse
import com.ngyu.swiftpay.security.provider.PaymentTokenProvider
import org.springframework.stereotype.Service

@Service
class PaymentApiKeyService(
  private val paymentTokenProvider: PaymentTokenProvider
) : PaymentApiKeyUseCase {
  override fun issueKey(): ApiKeyResponse {
    val key = paymentTokenProvider.issue()

    return ApiKeyResponse(key)
  }
}

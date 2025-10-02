package com.ngyu.swiftpay.payment.api.application

import com.ngyu.swiftpay.payment.api.dto.ApiKeyResponse

interface PaymentApiKeyUseCase {
  fun issueKey(): ApiKeyResponse
}

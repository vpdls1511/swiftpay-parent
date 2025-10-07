package com.ngyu.swiftpay.payment.api.application.usecase

import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials

interface PaymentApiKeyUseCase {
  fun issueKey(): PaymentCredentials
}

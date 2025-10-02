package com.ngyu.swiftpay.security.provider

import org.springframework.stereotype.Component

@Component
class PaymentTokenProvider {

  fun issue(): String {
    return "test-api-key"
  }
}

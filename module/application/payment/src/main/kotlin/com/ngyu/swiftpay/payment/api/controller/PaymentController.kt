package com.ngyu.swiftpay.payment.api.controller

import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import com.ngyu.swiftpay.payment.security.PaymentPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payment")
class PaymentController(
) {

  private val log = logger()

  @PostMapping("/health")
  fun processPayment(
    @PaymentPrincipal principal: PaymentCredentials
  ): String {
    return "ok"
  }
}

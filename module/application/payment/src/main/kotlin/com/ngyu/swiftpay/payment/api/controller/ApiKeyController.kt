package com.ngyu.swiftpay.payment.api.controller

import com.ngyu.swiftpay.payment.api.application.usecase.PaymentApiKeyUseCase
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/payment/api-keys"])
class ApiKeyController(
  private val paymentApiKeyUseCase: PaymentApiKeyUseCase,
) {

  @PostMapping(value = ["/issued"])
  fun issuedKey(): ResponseEntity<PaymentCredentials> {
    val response = paymentApiKeyUseCase.issueKey()
    return ResponseEntity.ok(response)
  }

}

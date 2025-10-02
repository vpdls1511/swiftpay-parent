package com.ngyu.swiftpay.payment.api.controller

import com.ngyu.swiftpay.payment.api.application.PaymentApiKeyUseCase
import com.ngyu.swiftpay.payment.api.dto.ApiKeyResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payment")
class PaymentController(
  private val paymentApiKeyUseCase: PaymentApiKeyUseCase,
) {

  @PostMapping(value = ["/key/issued"])
  fun issuedKey(): ResponseEntity<ApiKeyResponse> {
    val response = paymentApiKeyUseCase.issueKey()
    return ResponseEntity.ok(response)
  }

  @PostMapping(value = ["/key/valid"])
  fun validKey(
    @RequestHeader("X-SWIFT-PAY-KEY") apiKey: String
  ): ResponseEntity<ApiKeyResponse> {
    val response = paymentApiKeyUseCase.validKey(apiKey)
    return ResponseEntity.ok(response)
  }

}

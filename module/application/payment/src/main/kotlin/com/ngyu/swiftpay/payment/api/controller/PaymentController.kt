package com.ngyu.swiftpay.payment.api.controller

import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.payment.api.application.usecase.PaymentUseCase
import com.ngyu.swiftpay.payment.api.dto.OrderCreateRequestDto
import com.ngyu.swiftpay.payment.api.dto.OrderCreateResponseDto
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import com.ngyu.swiftpay.payment.security.PaymentPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Payment API")
@RestController
@RequestMapping("/payment")
class PaymentController(
  private val paymentUseCase: PaymentUseCase
) {

  private val log = logger()

  @Operation(summary = "결제 요청", description = "테스트 은행으로 결제 요청을 보냅니다.")
  @PostMapping("/order")
  fun processPayment(
    @PaymentPrincipal principal: PaymentCredentials,
    @RequestBody request: OrderCreateRequestDto
  ): ResponseEntity<OrderCreateResponseDto> {
    val response = paymentUseCase.readyOrder(request)
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(response)
  }
}

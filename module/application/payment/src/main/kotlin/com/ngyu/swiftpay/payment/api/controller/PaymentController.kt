package com.ngyu.swiftpay.payment.api.controller

import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.payment.api.application.usecase.PaymentUseCase
import com.ngyu.swiftpay.payment.api.dto.*
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

  @Operation(summary = "주문서 생성", description = "결제 전 주문 정보를 생성하고, orderId를 발급합니다.")
  @PostMapping("/order")
  fun processPayment(
    @PaymentPrincipal principal: PaymentCredentials,
    @RequestBody request: OrderCreateRequestDto
  ): ResponseEntity<OrderCreateResponseDto> {
    log.info("주문서 생성 시작")
    val response = paymentUseCase.readyOrder(request)
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(response)
  }

  @PostMapping
  fun confirmPayment(
    @PaymentPrincipal principal: PaymentPrincipal,
    @RequestBody request: PaymentRequestDto
  ): ResponseEntity<PaymentResponseDto> {
    log.info("결제 처리 시작")
    val response = paymentUseCase.processing(request)
    return ResponseEntity
      .status(HttpStatus.ACCEPTED)
      .body(response)
  }
}

package com.ngyu.swiftpay.payment.api.controller

import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.payment.api.dto.*
import com.ngyu.swiftpay.payment.application.service.payment.PaymentService
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
  private val paymentService: PaymentService
) {

  private val log = logger()

  @Operation(summary = "주문서 생성", description = "결제 전 주문 정보를 생성하고, orderId를 발급합니다.")
  @PostMapping("/order")
  fun processPayment(
    @PaymentPrincipal principal: PaymentCredentials,
    @RequestBody request: OrderCreateRequestDto
  ): ResponseEntity<OrderCreateResponseDto> {
    log.info("주문서 생성 시작")
    val response = paymentService.readyOrder(request)
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(response)
  }

  @Operation(
    summary = "결제 처리",
    description = """
        주문 정보를 기반으로 결제를 실행합니다.
        
        • CARD(카드): 즉시 승인/거절 응답 (동기 처리)
        • BANK_TRANSFER(계좌이체): 처리 접수 후 콜백으로 결과 전송 (비동기 처리)
        
        methodDetail의 type 필드로 CARD/BANK_TRANSFER를 구분하며, 각 타입에 맞는 필드를 전송해야 합니다.
        카드 결제는 할부, 포인트 사용 등의 옵션을 지원합니다.
    """
  )
  @PostMapping
  fun confirmPayment(
    @PaymentPrincipal principal: PaymentPrincipal,
    @RequestBody request: PaymentRequestDto
  ): ResponseEntity<PaymentResponseDto> {
    log.info("결제 처리 시작")
    val response = paymentService.processing(request)
    return ResponseEntity
      .status(HttpStatus.ACCEPTED)
      .body(response)
  }
}

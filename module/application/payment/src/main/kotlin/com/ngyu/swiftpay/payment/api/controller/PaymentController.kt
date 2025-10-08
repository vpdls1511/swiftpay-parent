package com.ngyu.swiftpay.payment.api.controller

import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import com.ngyu.swiftpay.payment.security.PaymentPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Payment API")
@RestController
@RequestMapping("/payment")
class PaymentController(
) {

  private val log = logger()

  @Operation(
    summary = "API 키 유효성 검사",
    description = "API 키가 유효한지 확인합니다."
  )
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "API 키 유효",
        content = [
          Content(
            mediaType = "text/plain",
            schema = Schema(implementation = String::class, example = "ok")
          )
        ]
      ),
      ApiResponse(
        responseCode = "401",
        description = "인증 실패 - 유효하지 않은 API 키",
        content = [Content(mediaType = "application/json")]
      ),
      ApiResponse(
        responseCode = "400",
        description = "잘못된 요청",
        content = [Content(mediaType = "application/json")]
      )
    ]
  )
  @PostMapping("/health")
  fun processPayment(
    @PaymentPrincipal principal: PaymentCredentials
  ): String {
    return "ok"
  }
}

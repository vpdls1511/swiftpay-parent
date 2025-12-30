package com.ngyu.swiftpay.payment.api.controller

import com.ngyu.swiftpay.payment.api.controller.dto.*
import com.ngyu.swiftpay.payment.application.service.merchant.MerchantService
import com.ngyu.swiftpay.payment.security.PaymentPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Merchant API")
@RestController
@RequestMapping("/merchant")
class MerchantController(
  private val merchantService: MerchantService
) {
  @Operation(summary = "가맹점 API키 재발행", description = "가맹점의 API키를 조회할 수 있다.")
  @PostMapping("/info")
  fun getMerchantKey(
    @PaymentPrincipal principal: PaymentCredentials,
    @RequestBody request: MerchantInfoRequest
  ): ResponseEntity<MerchantRegisterResponseDto> {
    val response = merchantService.getMerchantInfo(principal, request)

    return ResponseEntity.ok(response)
  }

  @PostMapping("/register")
  @Operation(summary = "가맹점 등록", description = "가맹점을 등록할 수 있습니다. 원래는 승인이 필요하나, 개발단계에서는 등록과 동시에 자동승인")
  fun register(
    @RequestBody request: MerchantRegisterReqeust
  ): ResponseEntity<MerchantRegisterResponseDto> {
    val response = merchantService.register(request)
    return ResponseEntity.ok(response)
  }

  @PutMapping("/webhook-url")
  @Operation(summary = "가맹점 정산 알림 웹훅 등록")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun putSettleWebhookUrl(
    @RequestBody request: PutWebhookUrlRequest
  ): ResponseEntity<Void> {
    merchantService.pugSettleWebhookUrl(request)
    return ResponseEntity.ok().build()
  }

}

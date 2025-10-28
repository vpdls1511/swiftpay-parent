package com.ngyu.swiftpay.payment.api.controller

import com.ngyu.swiftpay.payment.api.dto.MerchantRegisterReqeust
import com.ngyu.swiftpay.payment.api.dto.MerchantRegisterResponseDto
import com.ngyu.swiftpay.payment.application.service.merchant.MerchantService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Merchant API")
@RestController
@RequestMapping("/merchant")
class MerchantController(
  private val merchantService: MerchantService
) {

  @PostMapping("/register")
  @Operation(summary = "가맹점 등록", description = "가맹점을 등록할 수 있습니다. 원래는 승인이 필요하나, 개발단계에서는 등록과 동시에 자동승인")
  fun register(
    @RequestBody request: MerchantRegisterReqeust
  ): ResponseEntity<MerchantRegisterResponseDto> {
    val response = merchantService.register(request)
    return ResponseEntity.ok(response)
  }

}

package com.ngyu.swiftpay.payment.api.controller

import com.ngyu.swiftpay.payment.api.dto.BankAccountCreateDto
import com.ngyu.swiftpay.payment.api.dto.BankAccountResponseDto
import com.ngyu.swiftpay.payment.application.service.BankService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping(value = ["/bank"])
class BankController(
  val bankService: BankService
) {

  @PostMapping("/accounts")
  fun createAccount(
    @Valid @RequestBody request: BankAccountCreateDto
  ): ResponseEntity<BankAccountResponseDto> {
    val response = bankService.create(request)
    return ResponseEntity
      .created(URI.create("/bank/accounts/${response.accountNumber}"))
      .body(response)
  }
}

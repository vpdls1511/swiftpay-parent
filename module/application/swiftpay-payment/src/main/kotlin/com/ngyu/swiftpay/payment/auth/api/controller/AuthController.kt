package com.ngyu.swiftpay.payment.auth.api.controller

import com.ngyu.swiftpay.payment.auth.api.dto.AuthRegisterRequest
import com.ngyu.swiftpay.payment.auth.api.dto.AuthRegisterResponse
import com.ngyu.swiftpay.core.domain.Member
import com.ngyu.swiftpay.payment.auth.usecase.AuthenticationUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
  private val authenticationUseCase: AuthenticationUseCase,
) {

  @PostMapping(value = ["/register"])
  fun createUser(
    @RequestBody @Valid request: AuthRegisterRequest
  ): ResponseEntity<AuthRegisterResponse> {
    val member: Member = request.toDomain()
    val accessToken: String = authenticationUseCase.registerUser(member)

    return ResponseEntity.ok(AuthRegisterResponse(accessToken))
  }
}

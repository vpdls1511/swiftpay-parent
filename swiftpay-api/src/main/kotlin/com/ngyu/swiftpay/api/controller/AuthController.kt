package com.ngyu.swiftpay.api.controller

import com.ngyu.swiftpay.api.dto.AuthRegisterResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController(value = "auth")
class AuthController {

  @PostMapping(value = ["/register"])
  fun createUser(): ResponseEntity<AuthRegisterResponse> {
    return ResponseEntity.ok(AuthRegisterResponse("apiToken"))
  }
}

package me.ngyu.swiftpay.auth.api

import me.ngyu.swiftpay.auth.api.dto.AccountRequest
import me.ngyu.swiftpay.auth.application.usecase.AccountUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/v1/api/account")
class AccountController(
  private val accountUseCase: AccountUseCase,
) {

  @PostMapping("/register")
  fun register(@RequestBody request: AccountRequest): ResponseEntity<Void> {
    val uuid = accountUseCase.saveUser(request)
    return ResponseEntity.created(URI.create("/accounts/${uuid}")).build()
  }

}

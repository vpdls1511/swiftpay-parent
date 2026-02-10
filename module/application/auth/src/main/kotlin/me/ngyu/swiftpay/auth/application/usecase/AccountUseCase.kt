package me.ngyu.swiftpay.auth.application.usecase

import me.ngyu.swiftpay.auth.api.dto.AccountRequest
import me.ngyu.swiftpay.auth.api.dto.LoginRequest
import me.ngyu.swiftpay.auth.api.dto.TokenDto

interface AccountUseCase {
  fun saveUser(request: AccountRequest): String
  fun login(request: LoginRequest): TokenDto
}

package me.ngyu.swiftpay.auth.application.usecase

import me.ngyu.swiftpay.auth.application.api.dto.AccountRequest

interface AccountUseCase {
  fun saveUser(request: AccountRequest): String
}

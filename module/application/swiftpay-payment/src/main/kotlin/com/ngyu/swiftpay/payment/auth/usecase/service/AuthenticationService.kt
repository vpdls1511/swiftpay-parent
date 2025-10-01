package com.ngyu.swiftpay.payment.auth.usecase.service

import com.ngyu.swiftpay.core.domain.Member
import com.ngyu.swiftpay.payment.auth.usecase.AuthenticationUseCase
import org.springframework.stereotype.Service

@Service
class AuthenticationService: AuthenticationUseCase {
  override fun registerUser(member: Member): String {
    return "accessToken"
  }
}

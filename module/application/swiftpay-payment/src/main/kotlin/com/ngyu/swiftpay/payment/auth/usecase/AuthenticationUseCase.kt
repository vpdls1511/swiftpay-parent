package com.ngyu.swiftpay.payment.auth.usecase

import com.ngyu.swiftpay.core.domain.Member

interface AuthenticationUseCase {
  fun registerUser(member: Member): String
}

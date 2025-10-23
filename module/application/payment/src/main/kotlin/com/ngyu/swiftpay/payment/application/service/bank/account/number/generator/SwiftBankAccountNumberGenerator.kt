package com.ngyu.swiftpay.payment.application.service.bank.account.number.generator

import com.ngyu.swiftpay.core.domain.bank.BankCode
import org.springframework.stereotype.Component

@Component
class SwiftBankAccountNumberGenerator: AccountNumberGenerator {
  override fun generate(): String {
    TODO("Not yet implemented")
  }

  override fun validate(accountNumber: String): Boolean {
    TODO("Not yet implemented")
  }

  override fun supportedBankCode(): BankCode {
    return BankCode.SWIFT
  }
}

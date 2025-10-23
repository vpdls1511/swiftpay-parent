package com.ngyu.swiftpay.payment.application.generator.bank.account.number

import com.ngyu.swiftpay.core.domain.bank.BankCode

interface AccountNumberGenerator {
  fun generate(): String
  fun validate(accountNumber: String): Boolean
  fun toString(accountNumber: String): String

  fun supportedBankCode(): BankCode
}

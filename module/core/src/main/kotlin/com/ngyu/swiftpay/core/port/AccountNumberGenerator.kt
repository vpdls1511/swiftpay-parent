package com.ngyu.swiftpay.core.port

import com.ngyu.swiftpay.core.domain.bank.BankCode

interface AccountNumberGenerator {
  fun generate(): String
  fun validate(accountNumber: String): Boolean
  fun format(accountNumber: String): String

  fun supportedBankCode(): BankCode
}

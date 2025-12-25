package com.ngyu.swiftpay.core.port.repository

import com.ngyu.swiftpay.core.domain.bank.BankAccount

interface BankAccountRepository {
  fun save(domain: BankAccount): BankAccount
  fun findByBankAccount(domain: BankAccount): BankAccount

  fun existByBankAccount(accountNumber: String): Boolean
}

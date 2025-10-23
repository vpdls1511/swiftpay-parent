package com.ngyu.swiftpay.core.domain.bank

interface BankAccountRepository {
  fun save(domain: BankAccount): BankAccount
  fun findByBankAccount(domain: BankAccount): BankAccount
}

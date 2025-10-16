package com.ngyu.swiftpay.core.domain.bank

import java.math.BigDecimal
import java.time.LocalDateTime

data class Bank(
  val id: String,

  val bankCode: String,
  val bankName: String,

  val accountNumber: String,
  val accountHolder: String,

  val balance: BigDecimal,

  val status: BankAccountStatus = BankAccountStatus.ACTIVE,

  val createdAt: LocalDateTime = LocalDateTime.now(),
  val updatedAt: LocalDateTime = LocalDateTime.now()
) {
  // 출금 가능 여부 확인
  fun canWithdraw(amount: BigDecimal): Boolean {
    return status == BankAccountStatus.ACTIVE && balance >= amount
  }

  // 출금 처리
  fun withdraw(amount: BigDecimal): Bank {
    require(canWithdraw(amount)) { "잔액이 부족하거나 계좌 상태가 비활성입니다" }
    return this.copy(
      balance = balance - amount,
      updatedAt = LocalDateTime.now()
    )
  }

  // 입금 처리
  fun deposit(amount: BigDecimal): Bank {
    require(status == BankAccountStatus.ACTIVE) { "계좌 상태가 비활성입니다" }
    return this.copy(
      balance = balance + amount,
      updatedAt = LocalDateTime.now()
    )
  }
}

enum class BankAccountStatus {
  ACTIVE,      // 정상
  SUSPENDED,   // 정지
  CLOSED       // 해지
}

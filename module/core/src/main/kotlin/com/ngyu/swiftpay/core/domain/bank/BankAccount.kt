package com.ngyu.swiftpay.core.domain.bank

import com.ngyu.swiftpay.core.domain.money.Money
import java.time.LocalDateTime

data class BankAccount(
  val id: Long? = null,

  val bankCode: BankCode,

  val accountNumber: String,
  val accountHolder: String,

  val amount: Money,

  val status: BankAccountStatus = BankAccountStatus.ACTIVE,

  val createdAt: LocalDateTime = LocalDateTime.now(),
  val updatedAt: LocalDateTime = LocalDateTime.now()
) {

  companion object {
    fun create(bankCode: BankCode,
               holder: String,
               accountNumber: String): BankAccount {
      return BankAccount(
        bankCode = bankCode,
        accountHolder = holder,
        accountNumber = accountNumber,
        amount = Money.ZERO
      )
    }
  }

  // 출금 가능 여부 확인
  fun canWithdraw(other: Money): Boolean {
    return status == BankAccountStatus.ACTIVE && amount >= other
  }

  // 출금 처리
  fun withdraw(other: Money): BankAccount {
    require(canWithdraw(other)) { "잔액이 부족하거나 계좌 상태가 비활성입니다" }
    return this.copy(
      amount = amount - other,
      updatedAt = LocalDateTime.now()
    )
  }

  // 입금 처리
  fun deposit(other: Money): BankAccount {
    require(status == BankAccountStatus.ACTIVE) { "계좌 상태가 비활성입니다" }
    return this.copy(
      amount = amount + other,
      updatedAt = LocalDateTime.now()
    )
  }
}

enum class BankAccountStatus {
  ACTIVE,      // 정상
  SUSPENDED,   // 정지
  CLOSED       // 해지
}



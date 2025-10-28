package com.ngyu.swiftpay.core.domain.bank

import com.ngyu.swiftpay.core.common.exception.InvalidBankStatusException
import com.ngyu.swiftpay.core.domain.BaseDomain
import com.ngyu.swiftpay.core.vo.Money
import java.time.LocalDateTime

class BankAccount(
  override val id: Long? = null,

  val bankCode: BankCode,

  val accountNumber: String,
  val accountHolder: String,

  val amount: Money,

  val status: BankAccountStatus = BankAccountStatus.ACTIVE,

  val createdAt: LocalDateTime = LocalDateTime.now(),
  val updatedAt: LocalDateTime = LocalDateTime.now()
): BaseDomain<Long>() {

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

  // 출금 처리
  fun withdraw(other: Money): BankAccount {
    if (!canWithdraw(other)) {
      throw InvalidBankStatusException("잔액이 부족하거나 계좌 상태가 비활성입니다")
    }
    return this.copy(
      amount = amount - other,
      updatedAt = LocalDateTime.now()
    )
  }

  // 입금 처리
  fun deposit(other: Money): BankAccount {
    if (status != BankAccountStatus.ACTIVE) {
      throw InvalidBankStatusException("계좌 상태가 비활성입니다")
    }
    return this.copy(
      amount = amount + other,
      updatedAt = LocalDateTime.now()
    )
  }


  // 출금 가능 여부 확인
  private fun canWithdraw(other: Money): Boolean {
    return status == BankAccountStatus.ACTIVE && amount >= other
  }

  private fun copy(
    id: Long? = this.id,
    bankCode: BankCode = this.bankCode,
    accountNumber: String = this.accountNumber,
    accountHolder: String = this.accountHolder,
    amount: Money = this.amount,
    status: BankAccountStatus = this.status,
    createdAt: LocalDateTime = this.createdAt,
    updatedAt: LocalDateTime = this.updatedAt
  ): BankAccount {
    return BankAccount(
      id = id,
      bankCode = bankCode,
      accountNumber = accountNumber,
      accountHolder = accountHolder,
      amount = amount,
      status = status,
      createdAt = createdAt,
      updatedAt = updatedAt,
    )
  }
}

enum class BankAccountStatus {
  ACTIVE,      // 정상
  SUSPENDED,   // 정지
  CLOSED       // 해지
}



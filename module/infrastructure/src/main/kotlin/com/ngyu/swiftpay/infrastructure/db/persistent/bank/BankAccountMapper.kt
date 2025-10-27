package com.ngyu.swiftpay.infrastructure.db.persistent.bank

import com.ngyu.swiftpay.core.domain.bank.BankAccount
import com.ngyu.swiftpay.core.vo.Money

object  BankAccountMapper {
  fun toEntity(domain: BankAccount): BankAccountEntity {
    return BankAccountEntity(
      id = domain.id,
      bankCode = domain.bankCode,
      accountNumber = domain.accountNumber,
      accountHolder = domain.accountHolder,
      amount = domain.amount.toBigDecimal(),
      currency = domain.amount.currency,
      status = domain.status
    )
  }

  fun toDomain(entity: BankAccountEntity): BankAccount {
    return BankAccount(
      id = entity.id,
      bankCode = entity.bankCode,
      accountNumber = entity.accountNumber,
      accountHolder = entity.accountHolder,
      amount = Money(entity.amount, entity.currency),
      status = entity.status,
      createdAt = entity.createdAt,
      updatedAt = entity.updatedAt
    )
  }
}

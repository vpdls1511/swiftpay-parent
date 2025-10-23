package com.ngyu.swiftpay.infrastructure.db.persistent.settlement

import com.ngyu.swiftpay.core.domain.money.Currency
import com.ngyu.swiftpay.core.domain.money.Money
import com.ngyu.swiftpay.core.domain.settlement.Settlement

object SettlementMapper {

  fun toDomain(entity: SettlementEntity): Settlement {
    return Settlement(
      id = entity.id,
      settlementId = entity.settlementId,
      merchantAccountNumber = entity.merchantAccountNumber,
      merchantName = entity.merchantName,
      totalAmount = Money(entity.totalAmount, Currency.valueOf(entity.currency)),
      feeAmount = Money(entity.feeAmount, Currency.valueOf(entity.currency)),
      settlementAmount = Money(entity.settlementAmount, Currency.valueOf(entity.currency)),
      settlementDate = entity.settlementDate,
      status = entity.status,
      failReason = entity.failReason,
      createdAt = entity.createdAt,
      executedAt = entity.executedAt
    )
  }

  fun toEntity(domain: Settlement): SettlementEntity {
    return SettlementEntity(
      id = domain.id,
      settlementId = domain.settlementId,
      merchantAccountNumber = domain.merchantAccountNumber,
      merchantName = domain.merchantName,
      totalAmount = domain.totalAmount.amount,
      feeAmount = domain.feeAmount.amount,
      settlementAmount = domain.settlementAmount.amount,
      currency = domain.totalAmount.currency.name,
      settlementDate = domain.settlementDate,
      status = domain.status,
      failReason = domain.failReason,
      createdAt = domain.createdAt,
      executedAt = domain.executedAt
    )
  }
}

package com.ngyu.swiftpay.infrastructure.db.persistent.escrow

import com.ngyu.swiftpay.core.domain.escrow.Escrow
import com.ngyu.swiftpay.core.domain.money.Currency
import com.ngyu.swiftpay.core.domain.money.Money

object EscrowMapper {

  fun toDomain(entity: EscrowEntity): Escrow {
    return Escrow(
      id = entity.id,
      escrowId = entity.escrowId,
      paymentId = entity.paymentId,
      merchantId = entity.merchantId,
      amount = Money(entity.amount, Currency.valueOf(entity.currency)),
      status = entity.status,
      createdAt = entity.createdAt,
      completedAt = entity.completedAt,
      updatedAt = entity.updatedAt
    )
  }

  fun toEntity(domain: Escrow): EscrowEntity {
    return EscrowEntity(
      id = domain.id,
      escrowId = domain.escrowId,
      paymentId = domain.paymentId,
      merchantId = domain.merchantId,
      amount = domain.amount.amount,
      currency = domain.amount.currency.name,
      status = domain.status,
      createdAt = domain.createdAt,
      completedAt = domain.completedAt,
      updatedAt = domain.updatedAt
    )
  }
}

package com.ngyu.swiftpay.infrastructure.db.persistent.payment.mapper

import com.ngyu.swiftpay.core.domain.money.Money
import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.infrastructure.db.persistent.payment.PaymentEntity
import java.math.BigDecimal

object PaymentMapper {

  fun toEntity(domain: Payment): PaymentEntity {
    return PaymentEntity(
      id = domain.id,
      merchantId = domain.merchantId,
      orderId = domain.orderId,
      orderName = domain.orderName,
      amount = BigDecimal.valueOf(domain.amount.toLong()),
      currency = domain.amount.currency,
      method = domain.method,
      methodDetail = PayMethodDetailsEmbeddableMapper.toEmbeddable(domain.methodDetail),
      successUrl = domain.successUrl,
      cancelUrl = domain.cancelUrl,
      failureUrl = domain.failureUrl,
      status = domain.status,
      idempotencyKey = domain.idempotencyKey,
      settlementId = domain.settlementId,
    )
  }

  fun toDomain(entity: PaymentEntity): Payment {
    return Payment(
      id = entity.id,
      merchantId = entity.merchantId,
      orderId = entity.orderId,
      orderName = entity.orderName,
      amount = Money(entity.amount, entity.currency),
      method = entity.method,
      methodDetail = PayMethodDetailsEmbeddableMapper.toDomain(entity.methodDetail),
      successUrl = entity.successUrl,
      cancelUrl = entity.cancelUrl,
      failureUrl = entity.failureUrl,
      status = entity.status,
      idempotencyKey = entity.idempotencyKey,
      settlementId = entity.settlementId,
      createdAt = entity.createdAt,
      updatedAt = entity.updatedAt,
    )
  }
}

package com.ngyu.swiftpay.infrastructure.db.persistent.payment.mapper

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.infrastructure.db.persistent.payment.PaymentEntity

object PaymentMapper {

  fun toEntity(domain: Payment): PaymentEntity {
    return PaymentEntity(
      id = domain.id,
      apiPairKey = domain.apiPairKey,
      orderId = domain.orderId,
      orderName = domain.orderName,
      amount = domain.amount,
      currency = domain.currency,
      method = domain.method,
      methodDetail = PayMethodDetailsEmbeddableMapper.toEmbeddable(domain.methodDetail),
      successUrl = domain.successUrl,
      cancelUrl = domain.cancelUrl,
      failureUrl = domain.failureUrl,
      status = domain.status,
      idempotencyKey = domain.idempotencyKey
    )
  }

  fun toDomain(entity: PaymentEntity): Payment {
    return Payment(
      id = entity.id,
      apiPairKey = entity.apiPairKey,
      orderId = entity.orderId,
      orderName = entity.orderName,
      amount = entity.amount,
      currency = entity.currency,
      method = entity.method,
      methodDetail = PayMethodDetailsEmbeddableMapper.toDomain(entity.methodDetail),
      successUrl = entity.successUrl,
      cancelUrl = entity.cancelUrl,
      failureUrl = entity.failureUrl,
      status = entity.status,
      idempotencyKey = entity.idempotencyKey,
      createdAt = entity.createdAt,
      updatedAt = entity.updatedAt,
    )
  }
}

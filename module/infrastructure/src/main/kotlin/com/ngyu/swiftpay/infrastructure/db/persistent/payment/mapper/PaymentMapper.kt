package com.ngyu.swiftpay.infrastructure.db.persistent.payment.mapper

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.vo.Money
import com.ngyu.swiftpay.infrastructure.db.persistent.payment.PaymentEntity
import java.math.BigDecimal

object PaymentMapper {

  fun toEntity(domain: Payment): PaymentEntity {
    return PaymentEntity(
      id = domain.id,
      paymentId = domain.paymentId,
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
      reason = domain.reason,
      acquirerTransactionId = domain.acquirerTransactionId,
      acquirerApprovalNumber = domain.acquirerApprovalNumber,
      acquirerResponseCode = domain.acquirerResponseCode,
      acquirerMessage = domain.acquirerMessage,
      idempotencyKey = domain.idempotencyKey,
    )
  }

  fun toDomain(entity: PaymentEntity): Payment {
    return Payment(
      id = entity.id,
      paymentId = entity.paymentId,
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
      reason = entity.reason,
      acquirerTransactionId = entity.acquirerTransactionId,
      acquirerApprovalNumber = entity.acquirerApprovalNumber,
      acquirerResponseCode = entity.acquirerResponseCode,
      acquirerMessage = entity.acquirerMessage,
      idempotencyKey = entity.idempotencyKey,
      createdAt = entity.createdAt,
      updatedAt = entity.updatedAt,
    )
  }
}

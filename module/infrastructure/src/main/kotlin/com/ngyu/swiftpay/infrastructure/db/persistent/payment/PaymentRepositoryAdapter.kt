package com.ngyu.swiftpay.infrastructure.db.persistent.payment

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.port.PaymentRepository
import com.ngyu.swiftpay.infrastructure.db.persistent.payment.mapper.PaymentMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class PaymentRepositoryAdapter(
  val repository: PaymentJpaRepository
): PaymentRepository {
  override fun save(domain: Payment): Payment {
    val entity = PaymentMapper.toEntity(domain)
    val saveEntity = repository.saveAndFlush(entity)

    return PaymentMapper.toDomain(saveEntity)
  }

  override fun findByPayment(domain: Payment): Payment {
    val paymentId = domain.id
    requireNotNull(paymentId) { "Payment ID가 null입니다" }

    val entity = repository.findByIdOrNull(paymentId)
      ?: throw Exception("결제를 찾을 수 없습니다: $paymentId")

    return PaymentMapper.toDomain(entity)
  }
}

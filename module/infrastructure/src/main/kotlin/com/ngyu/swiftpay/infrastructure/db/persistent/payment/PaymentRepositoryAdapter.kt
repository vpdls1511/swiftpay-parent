package com.ngyu.swiftpay.infrastructure.db.persistent.payment

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.PaymentRepository
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

  override fun findByPaymentId(domain: Payment): Payment {
    val findEntity = repository.findByIdOrNull(domain.id)
      ?: throw Exception("결제건을 찾을 수 없습니다")

    return PaymentMapper.toDomain(findEntity)
  }
}

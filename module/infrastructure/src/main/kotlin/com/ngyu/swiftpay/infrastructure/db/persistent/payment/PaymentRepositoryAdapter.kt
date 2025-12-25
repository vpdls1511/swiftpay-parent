package com.ngyu.swiftpay.infrastructure.db.persistent.payment

import com.ngyu.swiftpay.core.common.exception.PaymentPersistenceException
import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.port.repository.PaymentRepository
import com.ngyu.swiftpay.infrastructure.db.persistent.payment.mapper.PaymentMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class PaymentRepositoryAdapter(
  val repository: PaymentJpaRepository
): PaymentRepository {
  override fun save(domain: Payment): Payment {
    val entity = PaymentMapper.toEntity(domain)
    val saveEntity = repository.save(entity)

    return PaymentMapper.toDomain(saveEntity)
  }

  override fun findByPayment(domain: Payment): Payment {
    val paymentId = domain.id
    val entity = repository.findByIdOrNull(paymentId)
      ?: throw PaymentPersistenceException("결제를 찾을 수 없습니다: $paymentId")

    return PaymentMapper.toDomain(entity)
  }
}

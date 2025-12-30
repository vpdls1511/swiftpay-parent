package com.ngyu.swiftpay.infrastructure.db.persistent.payment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentJpaRepository: JpaRepository<PaymentEntity, Long> {
  fun findByPaymentId(paymentId: String): PaymentEntity?
}

package com.ngyu.swiftpay.infrastructure.db.persistent.payment

import com.ngyu.swiftpay.core.domain.payment.PaymentRepository
import org.springframework.stereotype.Component

@Component
class PaymentRepositoryAdapter(
  val repository: PaymentJpaRepository
): PaymentRepository {
}

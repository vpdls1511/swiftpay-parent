package com.ngyu.swiftpay.infrastructure.db.persistent.payment

import org.springframework.stereotype.Component

@Component
class PaymentRepositoryAdapter(
  val repository: PaymentJpaRepository
): PaymentRepository{
}

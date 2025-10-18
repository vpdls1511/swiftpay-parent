package com.ngyu.swiftpay.core.domain.payment

interface PaymentRepository {
  fun save(domain: Payment): Payment
  fun findByPaymentId(domain: Payment): Payment
}

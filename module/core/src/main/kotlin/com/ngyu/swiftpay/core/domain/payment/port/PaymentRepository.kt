package com.ngyu.swiftpay.core.domain.payment.port

import com.ngyu.swiftpay.core.domain.payment.model.Payment

interface PaymentRepository {
  fun save(domain: Payment): Payment
  fun findByPayment(domain: Payment): Payment
}

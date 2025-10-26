package com.ngyu.swiftpay.core.port

import com.ngyu.swiftpay.core.domain.payment.Payment

interface PaymentRepository {
  fun save(domain: Payment): Payment
  fun findByPayment(domain: Payment): Payment
}

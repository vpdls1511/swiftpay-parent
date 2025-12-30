package com.ngyu.swiftpay.core.port.repository

import com.ngyu.swiftpay.core.domain.payment.Payment

interface PaymentRepository {
  fun save(domain: Payment): Payment
  fun findByPayment(domain: Payment): Payment
  fun findByPaymentId(paymentId: String): Payment
}

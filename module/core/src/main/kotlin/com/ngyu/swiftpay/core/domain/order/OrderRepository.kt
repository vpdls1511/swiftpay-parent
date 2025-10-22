package com.ngyu.swiftpay.core.domain.order

interface OrderRepository {
  fun save(domain: Order): Order
  fun findByPayment(domain: Order): Order
}

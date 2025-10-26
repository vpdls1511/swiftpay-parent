package com.ngyu.swiftpay.core.port

import com.ngyu.swiftpay.core.domain.order.Order

interface OrderRepository {
  fun save(domain: Order): Order
  fun findByPayment(domain: Order): Order
}

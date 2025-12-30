package com.ngyu.swiftpay.core.port.repository

import com.ngyu.swiftpay.core.domain.order.Order

interface OrderRepository {
  fun save(domain: Order): Order
  fun findByOrder(orderId: String): Order
  fun findByPayment(domain: Order): Order
}

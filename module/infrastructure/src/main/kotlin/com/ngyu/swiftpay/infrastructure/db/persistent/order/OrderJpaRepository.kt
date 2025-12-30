package com.ngyu.swiftpay.infrastructure.db.persistent.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderJpaRepository: JpaRepository<OrderEntity, Long> {
  fun findByOrderId(orderId: String): OrderEntity?
}

package com.ngyu.swiftpay.infrastructure.db.persistent.order

import com.ngyu.swiftpay.core.domain.order.Order
import com.ngyu.swiftpay.core.domain.order.OrderRepository
import com.ngyu.swiftpay.infrastructure.db.persistent.order.mapper.OrderMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class OrderRepositoryAdapter(
  private val repository: OrderJpaRepository
): OrderRepository {
  override fun save(domain: Order): Order {
    val entity = OrderMapper.toEntity(domain)
    val savedEntity = repository.save(entity)

    return OrderMapper.toDomain(savedEntity)
  }

  override fun findByPayment(domain: Order): Order {
    val orderId = domain.id
    requireNotNull(orderId) { "Settlement id가 Null 입니다" }

    val entity = repository.findByIdOrNull(orderId)
      ?: throw Exception("결제를 찾을 수 없습니다: $orderId")

    return OrderMapper.toDomain(entity)  }

}

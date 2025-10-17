package com.ngyu.swiftpay.core.domain.order

import com.ngyu.swiftpay.core.domain.money.Money
import java.time.LocalDateTime
import java.util.*

data class Order(
  val orderId: String,
  val merchantId: String,

  val orderName: String,
  val totalAmount: Money,

  val status: OrderStatus,

  // 고객 정보
  val customerName: String? = null,
  val customerEmail: String? = null,
  val customerPhone: String? = null,

  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
) {
  companion object {
    fun create(
      merchantId: String,
      orderName: String,
      totalAmount: Money,
      customerName: String? = null,
      customerEmail: String? = null,
      customerPhone: String? = null
    ): Order {
      return Order(
        orderId = UUID.randomUUID().toString(),
        merchantId = merchantId,
        orderName = orderName,
        totalAmount = totalAmount,
        status = OrderStatus.READY,
        customerName = customerName,
        customerEmail = customerEmail,
        customerPhone = customerPhone,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
      )
    }

  }
  fun processing(): Order {
    require(this.status == OrderStatus.READY) { "주문 대기 상태가 아닙니다." }
    return this.copy(status = OrderStatus.PROCESSING)
  }

  fun done(): Order {
    require(this.status == OrderStatus.PROCESSING) { "주문 진행중 상태가 아닙니다." }
    return this.copy(
      status = OrderStatus.DONE,
      updatedAt = LocalDateTime.now()
    )
  }

  fun cancel(): Order {
    require(this.status == OrderStatus.PROCESSING) { "주문 진행중 상태가 아닙니다." }
    return this.copy(
      status = OrderStatus.CANCELLED,
      updatedAt = LocalDateTime.now()
    )
  }
}

enum class OrderStatus {
  READY,        // 주문 생성됨
  PROCESSING,   // 주문 진행중
  DONE,         // 결제 완료
  CANCELLED     // 취소됨
}

package com.ngyu.swiftpay.core.domain.order

import com.ngyu.swiftpay.core.domain.money.Currency
import com.ngyu.swiftpay.core.domain.money.Money
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class Order(
  val orderId: String,
  val merchantId: String,

  val orderName: String,

  val totalAmount: Money,
  val balanceAmount: Money,
  val supplyAmount: Money,
  val tax: Money,

  val status: OrderStatus,

  // 고객 정보
  val customerName: String? = null,
  val customerEmail: String? = null,
  val customerPhone: String? = null,

  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
) {

  companion object {
    val taxFee = 11L

    fun create(
      merchantId: String,
      orderName: String,
      totalAmount: Long,
      currency: Currency,
      customerName: String? = null,
      customerEmail: String? = null,
      customerPhone: String? = null
    ): Order {
      val amount = Money(BigDecimal.valueOf(totalAmount), currency)
      val tax = amount / taxFee
      val supplyAmount = amount - tax

      return Order(
        orderId = UUID.randomUUID().toString(),
        merchantId = merchantId,
        orderName = orderName,
        totalAmount = amount,
        balanceAmount = amount,
        supplyAmount = supplyAmount,
        tax = tax,
        status = OrderStatus.READY,
        customerName = customerName,
        customerEmail = customerEmail,
        customerPhone = customerPhone,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
      )
    }
  }

  fun refund(amount: Money): Order {
    require(status == OrderStatus.DONE || status == OrderStatus.PARTIAL_REFUNDED) { "환불 가능한 상태가 아닙니다." }
    require(balanceAmount >= amount) { "환불 금액이 환불 가능 금액보다 큽니다." }

    val refundTax = amount / taxFee
    val refundSupply = amount - refundTax
    val newBalance = this.balanceAmount - amount

    val status = if (newBalance == Money.ZERO) {
      OrderStatus.REFUNDED
    } else {
      OrderStatus.PARTIAL_REFUNDED
    }

    return this.copy(
      status = status,
      balanceAmount = newBalance,
      tax = this.tax - refundTax,
      supplyAmount = this.supplyAmount - refundSupply,
      updatedAt = LocalDateTime.now(),
    )
  }

  fun processing(): Order {
    require(this.status == OrderStatus.READY) { "주문 대기 상태가 아닙니다." }
    return this.copy(
      status = OrderStatus.PROCESSING,
      updatedAt = LocalDateTime.now()
    )
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
  READY,              // 주문 생성됨
  PROCESSING,         // 주문 진행중
  DONE,               // 결제 완료
  PARTIAL_REFUNDED,   // 부분 환불
  REFUNDED,           // 전액 환불
  CANCELLED,          // 취소됨
}

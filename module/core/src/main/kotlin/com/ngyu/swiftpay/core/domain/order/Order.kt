package com.ngyu.swiftpay.core.domain.order

import com.ngyu.swiftpay.core.common.exception.InvalidAmountException
import com.ngyu.swiftpay.core.common.exception.InvalidOrderStatusException
import com.ngyu.swiftpay.core.domain.BaseDomain
import com.ngyu.swiftpay.core.vo.Currency
import com.ngyu.swiftpay.core.vo.Money
import java.math.BigDecimal
import java.time.LocalDateTime

class Order(
  override val id: Long,
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
) : BaseDomain<Long>() {

  companion object {
    val PREFIX = "SWIFT_ORDER_"
    val PADDING_LENGTH = 8

    val taxFee = 11L

    fun createOrderId(seq: Long): String = "${PREFIX}${seq.toString().padStart(PADDING_LENGTH, '0')}"

    fun create(
      orderSeq: Long,
      orderId: String,
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
        id = orderSeq,
        orderId = orderId,
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
    if (status == OrderStatus.DONE || status == OrderStatus.PARTIAL_REFUNDED) {
      throw InvalidOrderStatusException("환불 가능한 상태가 아닙니다.")
    }
    if (balanceAmount >= amount) {
      throw InvalidAmountException("환불 금액이 환불 가능 금액보다 큽니다.")
    }

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
      supplyAmount = this.supplyAmount - refundSupply
    )
  }

  fun processing(): Order {
    if (this.status == OrderStatus.READY) {
      throw InvalidOrderStatusException("주문 대기 상태가 아닙니다.")
    }
    return this.copy(
      status = OrderStatus.PROCESSING
    )
  }

  fun done(): Order {
    if (this.status == OrderStatus.PROCESSING) {
      throw InvalidOrderStatusException("주문 진행중 상태가 아닙니다.")
    }
    return this.copy(
      status = OrderStatus.DONE
    )
  }

  fun cancel(): Order {
    if (this.status == OrderStatus.PROCESSING) {
      throw InvalidOrderStatusException("주문 진행중 상태가 아닙니다.")
    }
    return this.copy(
      status = OrderStatus.CANCELLED
    )
  }

  private fun copy(
    id: Long = this.id,
    orderId: String = this.orderId,
    merchantId: String = this.merchantId,
    orderName: String = this.orderName,
    totalAmount: Money = this.totalAmount,
    balanceAmount: Money = this.balanceAmount,
    supplyAmount: Money = this.supplyAmount,
    tax: Money = this.tax,
    status: OrderStatus = this.status,
    customerName: String? = this.customerName,
    customerEmail: String? = this.customerEmail,
    customerPhone: String? = this.customerPhone,
    createdAt: LocalDateTime = this.createdAt,
    updatedAt: LocalDateTime = this.updatedAt,
  ): Order {
    return Order(
      id = id,
      orderId = orderId,
      merchantId = merchantId,
      orderName = orderName,
      totalAmount = totalAmount,
      balanceAmount = balanceAmount,
      supplyAmount = supplyAmount,
      tax = tax,
      status = status,
      customerName = customerName,
      customerEmail = customerEmail,
      customerPhone = customerPhone,
      createdAt = createdAt,
      updatedAt = updatedAt,
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

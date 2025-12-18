package com.ngyu.swiftpay.infrastructure.db.persistent.order

import com.ngyu.swiftpay.core.domain.order.OrderStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(
  name = "orders",
  indexes = [
    Index(name = "idx_order_id", columnList = "order_id"),
    Index(name = "idx_merchant_id", columnList = "merchant_id"),
    Index(name = "idx_status", columnList = "status"),
    Index(name = "idx_created_at", columnList = "created_at")
  ]
)
class OrderEntity(
  @Id
  val id: Long,

  @Column(name = "order_id", nullable = false, unique = true, length = 100)
  val orderId: String,

  @Column(name = "merchant_id", nullable = false, length = 100)
  val merchantId: String,

  @Column(name = "order_name", nullable = false, length = 200)
  val orderName: String,

  @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
  val totalAmount: BigDecimal,

  @Column(name = "balance_amount", nullable = false, precision = 19, scale = 2)
  val balanceAmount: BigDecimal,

  @Column(name = "supply_amount", nullable = false, precision = 19, scale = 2)
  val supplyAmount: BigDecimal,

  @Column(name = "tax", nullable = false, precision = 19, scale = 2)
  val tax: BigDecimal,

  @Column(name = "currency", nullable = false, length = 3)
  val currency: String,

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  val status: OrderStatus,

  @Column(name = "customer_name", length = 100)
  val customerName: String? = null,

  @Column(name = "customer_email", length = 100)
  val customerEmail: String? = null,

  @Column(name = "customer_phone", length = 20)
  val customerPhone: String? = null,

  @Column(name = "created_at", nullable = false)
  val createdAt: LocalDateTime,

  @Column(name = "updated_at", nullable = false)
  val updatedAt: LocalDateTime
)

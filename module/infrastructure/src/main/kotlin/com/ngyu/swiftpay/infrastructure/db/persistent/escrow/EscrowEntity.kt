package com.ngyu.swiftpay.infrastructure.db.persistent.escrow

import com.ngyu.swiftpay.core.domain.escrow.EscrowStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "escrow")
class EscrowEntity(
  @Id
  @Column(name = "id")
  val id: Long? = null,

  @Column(name = "escrow_id", nullable = false, unique = true, length = 100)
  val escrowId: String,

  @Column(name = "payment_id", nullable = false, length = 100)
  val paymentId: String,

  @Column(name = "merchant_id", nullable = false, length = 100)
  val merchantId: String,

  @Column(name = "settlement_id")
  val settlementId: Long? = null,

  @Column(name = "amount", nullable = false, precision = 19, scale = 2)
  val amount: BigDecimal,

  @Column(name = "currency", nullable = false, length = 3)
  val currency: String,

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  val status: EscrowStatus,

  @Column(name = "created_at", nullable = false)
  val createdAt: LocalDateTime,

  @Column(name = "completed_at")
  val completedAt: LocalDateTime? = null,

  @Column(name = "updated_at", nullable = false)
  val updatedAt: LocalDateTime
)

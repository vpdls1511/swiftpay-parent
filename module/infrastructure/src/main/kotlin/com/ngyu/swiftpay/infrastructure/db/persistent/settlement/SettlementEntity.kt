package com.ngyu.swiftpay.infrastructure.db.persistent.settlement

import com.ngyu.swiftpay.core.domain.settlement.SettlementStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "settlement")
class SettlementEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  val id: Long? = null,

  @Column(name = "settlement_id", nullable = false, unique = true, length = 100)
  val settlementId: String,

  @Column(name = "merchant_account_number", nullable = false, length = 50)
  val merchantAccountNumber: String,

  @Column(name = "merchant_name", nullable = false, length = 100)
  val merchantName: String,

  @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
  val totalAmount: BigDecimal,

  @Column(name = "fee_amount", nullable = false, precision = 19, scale = 2)
  val feeAmount: BigDecimal,

  @Column(name = "settlement_amount", nullable = false, precision = 19, scale = 2)
  val settlementAmount: BigDecimal,

  @Column(name = "currency", nullable = false, length = 3)
  val currency: String,

  @Column(name = "settlement_date", nullable = false)
  val settlementDate: LocalDate,

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  val status: SettlementStatus,

  @Column(name = "fail_reason", length = 500)
  val failReason: String? = null,

  @Column(name = "created_at", nullable = false)
  val createdAt: LocalDateTime,

  @Column(name = "executed_at")
  val executedAt: LocalDateTime? = null
)

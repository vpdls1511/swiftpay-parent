package com.ngyu.swiftpay.infrastructure.db.persistent.merchant

import com.ngyu.swiftpay.core.domain.merchant.MerchantStatus
import com.ngyu.swiftpay.core.domain.merchant.SettlementCycle
import com.ngyu.swiftpay.infrastructure.db.persistent.common.BaseTimeEntity
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "merchant")
class MerchantEntity(
  @Id
  @Column(name = "id")
  val id: Long,

  @Column(name = "merchant_id")
  val merchantId: String,

  @Column(name = "user_id")
  val userId: Long? = null,

  // 사업자 정보
  @Column(name = "business_number", nullable = false, unique = true)
  val businessNumber: String,
  @Column(name = "business_name", nullable = false)
  val businessName: String,
  @Column(name = "representative_name", nullable = false)
  val representativeName: String,
  @Column(name = "business_type", nullable = false)
  val businessType: String,
  @Column(name = "email", nullable = false)
  val email: String,
  @Column(name = "phone_number", nullable = false)
  val phoneNumber: String,
  @Column(name = "address", nullable = false)
  val address: String,

  @Column(name = "bank_account_number", nullable = false)
  val bankAccountNumber: String,
  @Column(name = "fee_rate", nullable = false, precision = 5, scale = 4)
  val feeRate: BigDecimal = BigDecimal.valueOf(0.03),
  @Enumerated(EnumType.STRING)
  @Column(name = "settlement_cycle", nullable = false)
  val settlementCycle: SettlementCycle = SettlementCycle.D_PLUS_1,

  @Column(name = "contract_start_date")
  val contractStartDate: LocalDate? = null,
  @Column(name = "contract_end_date")
  val contractEndDate: LocalDate? = null,

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  val status: MerchantStatus = MerchantStatus.PENDING,

  @Column(name = "approved_at")
  val approvedAt: LocalDateTime? = null

) : BaseTimeEntity()

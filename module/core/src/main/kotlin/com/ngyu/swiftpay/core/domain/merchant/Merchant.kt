package com.ngyu.swiftpay.core.domain.merchant

import com.ngyu.swiftpay.core.domain.BaseDomain
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Merchant(
  override val id: Long,

  val merchantId: String = UUID.randomUUID().toString(),
  val userId: Long? = null,

  // 사업자 정보
  val businessNumber: String,
  val businessName: String,
  val representativeName: String,
  val businessType: String,
  val email: String,
  val phoneNumber: String,
  val address: String,

  val apiPairKey: String? = null,

  val bankAccountNumber: String,
  val feeRate: BigDecimal = BigDecimal.valueOf(0.03), // 기본 3%

  val settlementCycle: SettlementCycle = SettlementCycle.D_PLUS_1,

  val status: MerchantStatus = MerchantStatus.PENDING,
  val suspendedReason: String? = null,

  val contractStartDate: LocalDate? = null,
  val contractEndDate: LocalDate? = null,

  val createdAt: LocalDateTime = LocalDateTime.now(),
  val updatedAt: LocalDateTime = LocalDateTime.now(),
  val approvedAt: LocalDateTime? = null
) : BaseDomain<Long>() {
  fun approved(startDate: LocalDate): Merchant {
    require(this.status == MerchantStatus.PENDING) { "승인 대기 상태가 아닙니다." }
    return this.copy(
      status = MerchantStatus.ACTIVE,
      contractStartDate = startDate,
      approvedAt = LocalDateTime.now()
    )
  }

  fun suspended(reason: String): Merchant {
    require(this.status == MerchantStatus.ACTIVE) { "활성 상태가 아닙니다" }
    return this.copy(
      status = MerchantStatus.SUSPENDED,
      suspendedReason = reason
    )
  }

  fun resume(): Merchant {
    require(this.status == MerchantStatus.SUSPENDED) { "정지 상태가 아닙니다" }
    return this.copy(
      status = MerchantStatus.ACTIVE
    )
  }

  fun terminate(endDate: LocalDate): Merchant {
    require(this.status != MerchantStatus.TERMINATED) { "이미 종료된 가맹점입니다" }
    return this.copy(
      status = MerchantStatus.TERMINATED,
      contractEndDate = endDate
    )
  }

  private fun copy(
    id: Long = this.id,
    merchantId: String = this.merchantId,
    userId: Long? = this.userId,
    businessNumber: String = this.businessNumber,
    businessName: String = this.businessName,
    representativeName: String = this.representativeName,
    businessType: String = this.businessType,
    email: String = this.bankAccountNumber,
    phoneNumber: String = this.bankAccountNumber,
    address: String = this.address,
    apiPairKey: String = this.bankAccountNumber,
    bankAccountNumber: String = this.bankAccountNumber,
    feeRate: BigDecimal = this.feeRate,
    settlementCycle: SettlementCycle = this.settlementCycle,
    status: MerchantStatus = this.status,
    suspendedReason: String? = this.suspendedReason,
    contractStartDate: LocalDate? = this.contractStartDate,
    contractEndDate: LocalDate? = this.contractEndDate,
    createdAt: LocalDateTime = this.createdAt,
    updatedAt: LocalDateTime = this.updatedAt,
    approvedAt: LocalDateTime? = this.approvedAt
  ): Merchant {
    return Merchant(
      id = id,
      merchantId = merchantId,
      userId = userId,
      businessNumber = businessNumber,
      businessName = businessName,
      representativeName = representativeName,
      businessType = businessType,
      email = email,
      phoneNumber = phoneNumber,
      address = address,
      apiPairKey = apiPairKey,
      bankAccountNumber = bankAccountNumber,
      feeRate = feeRate,
      settlementCycle = settlementCycle,
      status = status,
      suspendedReason = suspendedReason,
      contractStartDate = contractStartDate,
      contractEndDate = contractEndDate,
      createdAt = createdAt,
      updatedAt = updatedAt,
      approvedAt = approvedAt,
    )
  }

}

enum class SettlementCycle {
  D_PLUS_1, // 익일 정산
  WEEKLY,    // 주 단위
  MONTHLY    // 월 단위
}

enum class MerchantStatus {
  PENDING,
  ACTIVE,
  SUSPENDED,
  TERMINATED
}

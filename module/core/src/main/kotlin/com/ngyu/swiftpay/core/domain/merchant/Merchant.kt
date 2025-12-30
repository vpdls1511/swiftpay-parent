package com.ngyu.swiftpay.core.domain.merchant

import com.ngyu.swiftpay.core.common.exception.InvalidMerchantStatusException
import com.ngyu.swiftpay.core.domain.BaseDomain
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class Merchant(
  override val id: Long,

  val merchantId: String,
  val userId: Long? = null,

  // 사업자 정보
  val businessNumber: String,
  val businessName: String,
  val representativeName: String,
  val businessType: String,
  val email: String,
  val phoneNumber: String,
  val address: String,

  val bankAccountNumber: String,
  val feeRate: BigDecimal = BigDecimal.valueOf(0.03), // 기본 3%

  val settlementCycle: SettlementCycle = SettlementCycle.D_PLUS_1,

  val status: MerchantStatus = MerchantStatus.PENDING,

  val settleWebhookUrl: String? = null,

  val contractStartDate: LocalDate? = null,
  val contractEndDate: LocalDate? = null,

  val createdAt: LocalDateTime = LocalDateTime.now(),
  val updatedAt: LocalDateTime = LocalDateTime.now(),
  val approvedAt: LocalDateTime? = null
) : BaseDomain<Long>() {

  companion object {
    private const val PREFIX = "SWIFT_MERCH_"
    private const val PADDING_LENGTH = 8

    fun createMerchantId(seq: Long): String {
      return "$PREFIX${seq.toString().padStart(PADDING_LENGTH, '0')}"
    }
  }

  fun approved(startDate: LocalDate): Merchant {
    if (this.status != MerchantStatus.PENDING) {
      throw InvalidMerchantStatusException("승인 대기 상태가 아닙니다.")
    }
    return this.copy(
      status = MerchantStatus.ACTIVE,
      contractStartDate = startDate,
      approvedAt = LocalDateTime.now()
    )
  }

  fun setSettleWebhookUrl(url: String): Merchant {
    return this.copy(settleWebhookUrl = url)
  }

  fun suspended(): Merchant {
    if (this.status != MerchantStatus.ACTIVE) {
      throw InvalidMerchantStatusException("활성 상태가 아닙니다")
    }
    return this.copy(
      status = MerchantStatus.SUSPENDED,
    )
  }

  fun resume(): Merchant {
    if (this.status != MerchantStatus.SUSPENDED) {
      throw InvalidMerchantStatusException("정지 상태가 아닙니다")
    }
    return this.copy(
      status = MerchantStatus.ACTIVE
    )
  }

  fun terminate(endDate: LocalDate): Merchant {
    if (this.status != MerchantStatus.TERMINATED) {
      throw InvalidMerchantStatusException("이미 종료된 가맹점입니다")
    }
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
    bankAccountNumber: String = this.bankAccountNumber,
    feeRate: BigDecimal = this.feeRate,
    settlementCycle: SettlementCycle = this.settlementCycle,
    status: MerchantStatus = this.status,
    settleWebhookUrl: String? = this.settleWebhookUrl,
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
      bankAccountNumber = bankAccountNumber,
      feeRate = feeRate,
      settlementCycle = settlementCycle,
      status = status,
      settleWebhookUrl = settleWebhookUrl,
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

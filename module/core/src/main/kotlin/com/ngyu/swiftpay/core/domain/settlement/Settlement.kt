package com.ngyu.swiftpay.core.domain.settlement

import com.ngyu.swiftpay.core.common.exception.InvalidSettlementStatusException
import com.ngyu.swiftpay.core.domain.BaseDomain
import com.ngyu.swiftpay.core.vo.Money
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Settlement(
  override val id: Long? = null,
  val settlementId: String = UUID.randomUUID().toString(),
  val merchantAccountNumber: String,
  val merchantName: String,

  val totalAmount: Money, // 총 결제 금액
  val feeAmount: Money, // 총 수수료
  val settlementAmount: Money, // 실제 정산 금액

  val settlementDate: LocalDate, // 정산 예정일

  val status: SettlementStatus = SettlementStatus.PENDING,
  val failReason: String? = null,

  val createdAt: LocalDateTime = LocalDateTime.now(),
  val executedAt: LocalDateTime? = null
) : BaseDomain<Long>() {
  fun process(): Settlement {
    if (this.status != SettlementStatus.PENDING) {
      throw InvalidSettlementStatusException("정산 대기 상태가 아닙니다.")
    }
    return this.copy(
      status = SettlementStatus.PROCESSING
    )
  }

  fun complete(): Settlement {
    if (this.status != SettlementStatus.PROCESSING) {
      throw InvalidSettlementStatusException("정산 처리 중 상태가 아닙니다.")
    }
    return this.copy(
      status = SettlementStatus.COMPLETED,
      executedAt = LocalDateTime.now(),
    )
  }

  fun fail(reason: String): Settlement {
    if (this.status != SettlementStatus.PROCESSING) {
      throw InvalidSettlementStatusException("정산 처리 중 상태가 아닙니다.")
    }
    return this.copy(
      status = SettlementStatus.FAILED,
      failReason = reason,
      executedAt = LocalDateTime.now(),
    )
  }

  private fun copy(
    id: Long? = this.id,
    settlementId: String = this.settlementId,
    merchantAccountNumber: String = this.merchantAccountNumber,
    merchantName: String = this.merchantName,
    totalAmount: Money = this.totalAmount,
    feeAmount: Money = this.feeAmount,
    settlementAmount: Money = this.settlementAmount,
    settlementDate: LocalDate = this.settlementDate,
    status: SettlementStatus = this.status,
    failReason: String? = this.failReason,
    createdAt: LocalDateTime = this.createdAt,
    executedAt: LocalDateTime? = this.executedAt
  ): Settlement {
    return Settlement(
      id = id,
      settlementId = settlementId,
      merchantAccountNumber = merchantAccountNumber,
      merchantName = merchantName,
      totalAmount = totalAmount,
      feeAmount = feeAmount,
      settlementAmount = settlementAmount,
      settlementDate = settlementDate,
      status = status,
      failReason = failReason,
      createdAt = createdAt,
      executedAt = executedAt,
    )
  }
}

enum class SettlementStatus {
  PENDING,    // 정산 대기
  PROCESSING, // 정산 처리 중
  COMPLETED,  // 정산 완료
  FAILED      // 정산 실패
}

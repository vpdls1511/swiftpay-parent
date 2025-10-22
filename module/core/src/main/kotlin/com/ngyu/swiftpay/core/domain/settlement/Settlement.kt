package com.ngyu.swiftpay.core.domain.settlement

import com.ngyu.swiftpay.core.domain.money.Money
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class Settlement(
  val id: Long? = null,
  val settlementId: String = UUID.randomUUID().toString(),
  val merchantAccountNumber: String,
  val merchantName: String,

  val totalAmount: Money, // 총 결제 금액
  val feeAmount: Money, // 총 수수료
  val settlementAmount: Money, // 실제 정산 금액

  val paymentIds: List<Long>, // 포함된 결제 ID들
  val settlementDate: LocalDate, // 정산 예정일

  val status: SettlementStatus = SettlementStatus.PENDING,
  val failReason: String? = null,

  val createdAt: LocalDateTime = LocalDateTime.now(),
  val executedAt: LocalDateTime? = null
){

  init {
    require(this.paymentIds.isNotEmpty()) { "정산받을 내역이 존재하지 않습니다." }
  }

  fun process(): Settlement {
    require(this.status == SettlementStatus.PENDING) { "정산 대기 상태가 아닙니다." }
    return this.copy(
      status = SettlementStatus.PROCESSING
    )
  }

  fun complete(): Settlement {
    require(this.status == SettlementStatus.PROCESSING) { "정산 처리 중 상태가 아닙니다." }
    return this.copy(
      status = SettlementStatus.COMPLETED,
      executedAt = LocalDateTime.now(),
    )
  }

  fun fail(reason: String): Settlement {
    require(this.status == SettlementStatus.PROCESSING) { "정산 처리 중 상태가 아닙니다." }
    return this.copy(
      status = SettlementStatus.FAILED,
      failReason = reason,
      executedAt = LocalDateTime.now(),
    )
  }

  fun getPaymentCount(): Int = this.paymentIds.size
}

enum class SettlementStatus {
  PENDING,    // 정산 대기
  PROCESSING, // 정산 처리 중
  COMPLETED,  // 정산 완료
  FAILED      // 정산 실패
}

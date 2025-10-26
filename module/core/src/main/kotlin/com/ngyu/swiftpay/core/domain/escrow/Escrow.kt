package com.ngyu.swiftpay.core.domain.escrow

import com.ngyu.swiftpay.core.domain.money.Money
import com.ngyu.swiftpay.core.domain.payment.Payment
import java.time.LocalDateTime

data class Escrow(
  val id: Long? = null,
  val escrowId: String,
  val paymentId: String,
  val merchantId: String,
  val amount: Money,
  val status: EscrowStatus,

  val settlementId: Long? = null,


  val createdAt: LocalDateTime,
  val completedAt: LocalDateTime? = null,  // settle or refund 완료 시간
  val updatedAt: LocalDateTime
) {
  companion object {
    fun hold(payment: Payment): Escrow {
      val now = LocalDateTime.now()
      return Escrow(
        escrowId = generateEscrowId(),
        paymentId = payment.paymentId,
        merchantId = payment.merchantId,
        amount = payment.amount,
        status = EscrowStatus.HOLD,
        createdAt = now,
        updatedAt = now
      )
    }

    private fun generateEscrowId(): String {
      return "escrow_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }
  }

  fun settle(): Escrow {
    require(status == EscrowStatus.HOLD) { "HOLD 상태가 아닙니다." }
    val now = LocalDateTime.now()
    return copy(
      settlementId = settlementId,  // 추가!
      status = EscrowStatus.SETTLED,
      completedAt = now,
      updatedAt = now
    )
  }

  fun refund(): Escrow {
    require(status == EscrowStatus.HOLD) { "HOLD 상태가 아닙니다." }
    val now = LocalDateTime.now()
    return copy(
      status = EscrowStatus.REFUNDED,
      completedAt = now,
      updatedAt = now
    )
  }
}

enum class EscrowStatus {
  HOLD,
  SETTLED,
  REFUNDED
}

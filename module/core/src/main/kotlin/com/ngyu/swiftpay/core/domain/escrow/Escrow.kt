package com.ngyu.swiftpay.core.domain.escrow

import com.ngyu.swiftpay.core.domain.BaseDomain
import com.ngyu.swiftpay.core.domain.money.Money
import com.ngyu.swiftpay.core.domain.payment.Payment
import java.time.LocalDateTime

class Escrow(
  override val id: Long? = null,

  val escrowId: String,
  val paymentId: String,
  val merchantId: String,
  val amount: Money,
  val status: EscrowStatus,

  val settlementId: Long? = null,


  val createdAt: LocalDateTime,
  val completedAt: LocalDateTime? = null,  // settle or refund 완료 시간
  val updatedAt: LocalDateTime
) : BaseDomain<Long>() {
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
    )
  }

  private fun copy(
    id: Long? = this.id,
    escrowId: String = this.escrowId,
    paymentId: String = this.paymentId,
    merchantId: String = this.merchantId,
    amount: Money = this.amount,
    status: EscrowStatus = this.status,
    settlementId: Long? = this.settlementId,
    createdAt: LocalDateTime = this.createdAt,
    completedAt: LocalDateTime? = this.completedAt,
    updatedAt: LocalDateTime = LocalDateTime.now(),
  ): Escrow {
    return Escrow(
      id = id,
      escrowId = escrowId,
      paymentId = paymentId,
      merchantId = merchantId,
      amount = amount,
      status = status,
      settlementId = settlementId,
      createdAt = createdAt,
      completedAt = completedAt,
      updatedAt = updatedAt
    )
  }
}

enum class EscrowStatus {
  HOLD,
  SETTLED,
  REFUNDED
}

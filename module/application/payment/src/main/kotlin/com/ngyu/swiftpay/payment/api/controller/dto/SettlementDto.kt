package com.ngyu.swiftpay.payment.api.controller.dto

import com.ngyu.swiftpay.core.domain.settlement.Settlement
import java.time.LocalDate

data class ConfirmPaymentResponse(
  val settlementId: String,
  val totalAmount: Long,
  val feeAmount: Long,
  val settlementAmount: Long,
  val settlementDate: LocalDate,
) {
  companion object {
    fun create(settlement: Settlement): ConfirmPaymentResponse {
      return ConfirmPaymentResponse(
        settlementId = settlement.settlementId,
        totalAmount = settlement.totalAmount.amount.toLong(),
        feeAmount = settlement.feeAmount.amount.toLong(),
        settlementAmount = settlement.settlementAmount.amount.toLong(),
        settlementDate = settlement.settlementDate
      )
    }
  }
}

package com.ngyu.swiftpay.core.domain.payment.model

enum class PaymentCardType(
  val description: String,
  val code: String,
) {
  CREDIT("신용", "CREDIT"),
  DEBIT("체크", "DEBIT"),
  ;

  companion object {
    fun fromCode(code: String): PaymentCardType? {
      return entries.find { it.code == code }
    }

    fun fromDescription(description: String): PaymentCardType? {
      return entries.find { it.description == description }
    }
  }
}

package com.ngyu.swiftpay.payment.api.webhook.dto

data class SettlementEvent(
  val merchantId: String,
  val trnKey: String,
)

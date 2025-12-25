package com.ngyu.swiftpay.core.port.client.dto

data class CardApprovalResult(
  val isSuccess: Boolean,
  val transactionId: String,
  val approvalNumber: String,
  val code: String,
  val message: String
)

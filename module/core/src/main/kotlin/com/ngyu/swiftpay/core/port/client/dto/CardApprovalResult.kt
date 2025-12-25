package com.ngyu.swiftpay.core.port.client.dto

data class CardApprovalResult(
  val isSuccess: Boolean,
  val approvalNumber: String?,
  val transferNumber: String?,
  val message: String
)

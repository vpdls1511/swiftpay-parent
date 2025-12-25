package com.ngyu.swiftpay.core.port.client.dto

data class BankTransferResult(
  val isSuccess: Boolean,
  val transactionId: String,
  val transferNumber: String,
  val code: String,
  val message: String
)

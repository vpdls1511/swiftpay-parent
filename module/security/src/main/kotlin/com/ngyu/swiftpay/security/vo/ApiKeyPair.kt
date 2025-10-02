package com.ngyu.swiftpay.security.vo

data class ApiKeyPair(
  val plain: String,
  val lookupKey: String,
  val hashed: String
)

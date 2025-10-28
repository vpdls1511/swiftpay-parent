package com.ngyu.swiftpay.security.vo

data class ApiKeyPair(
  val plain: String,
  val apiPairKey: String,
  val hashed: String
)

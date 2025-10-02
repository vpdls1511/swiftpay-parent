package com.ngyu.swiftpay.core.domain.apiKey

import java.time.LocalDateTime

data class ApiKey(
  val apiKey: String,
  val limit: Int = 100000,
  val issuedAt: LocalDateTime,
  val expiresAt: LocalDateTime,
  val status: ApiKeyStatus
)

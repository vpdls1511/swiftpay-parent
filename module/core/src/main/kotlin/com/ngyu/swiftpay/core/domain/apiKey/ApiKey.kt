package com.ngyu.swiftpay.core.domain.apiKey

import java.time.LocalDateTime

data class ApiKey(
  val apiKey: String,
  val lookupKey: String,
  val userId: Long? = null,
  val callLimit: Int,
  val issuedAt: LocalDateTime,
  val expiresAt: LocalDateTime,
  val status: ApiKeyStatus
) {
  companion object {
    fun create(apiKey: String, lookupKey: String): ApiKey {
      val now = LocalDateTime.now()
      val defaultLimit = 100000
      val defaultExpiredWeek = 1L
      return ApiKey(
        apiKey = apiKey,
        lookupKey = lookupKey,
        userId = null,
        callLimit = defaultLimit,
        issuedAt = now,
        expiresAt = now.plusWeeks(defaultExpiredWeek),
        status = ApiKeyStatus.ACTIVE
      )
    }
  }
}

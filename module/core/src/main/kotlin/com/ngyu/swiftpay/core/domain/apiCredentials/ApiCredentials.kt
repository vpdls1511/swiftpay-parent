package com.ngyu.swiftpay.core.domain.apiCredentials

import java.time.LocalDateTime

data class ApiCredentials(
  val apiKey: String,
  val lookupKey: String,
  val userId: Long? = null,
  val callLimit: Int,
  val issuedAt: LocalDateTime,
  val expiresAt: LocalDateTime,
  val status: ApiKeyStatus
) {
  companion object {
    fun create(apiKey: String, lookupKey: String): ApiCredentials {
      val now = LocalDateTime.now()
      val defaultLimit = 100000
      val defaultExpiredWeek = 1L
      return ApiCredentials(
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

package com.ngyu.swiftpay.core.domain.apiCredentials

import java.time.LocalDateTime

data class ApiCredentials(
  val merchantId: Long,
  val apiKey: String,
  val lookupKey: String,
  val callLimit: Int,
  val issuedAt: LocalDateTime,
  val expiresAt: LocalDateTime,
  val status: ApiKeyStatus
) {
  companion object {
    fun create(merchantId: Long, apiKey: String, lookupKey: String): ApiCredentials {
      val now = LocalDateTime.now()
      val defaultLimit = 100000
      val defaultExpiredWeek = 1L
      return ApiCredentials(
        merchantId = merchantId,
        apiKey = apiKey,
        lookupKey = lookupKey,
        callLimit = defaultLimit,
        issuedAt = now,
        expiresAt = now.plusWeeks(defaultExpiredWeek),
        status = ApiKeyStatus.ACTIVE
      )
    }
  }

  /**
   * api key는 sha256으로 hash 되어있어 복호화 불가능
   */
  fun update(apiKey: String, apiPairKey: String): ApiCredentials {
    return this.copy(
      apiKey = apiKey,
      lookupKey = apiPairKey,
    )
  }
}

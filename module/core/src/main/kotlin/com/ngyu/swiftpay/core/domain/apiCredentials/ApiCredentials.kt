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
    private const val DEFAULT_LIMIT = 100000
    private const val DEFAULT_EXPIRED_WEEKS = 1L

    fun create(merchantId: Long, apiKey: String, lookupKey: String): ApiCredentials {
      val now = LocalDateTime.now()
      return ApiCredentials(
        merchantId = merchantId,
        apiKey = apiKey,
        lookupKey = lookupKey,
        callLimit = DEFAULT_LIMIT,
        issuedAt = now,
        expiresAt = now.plusWeeks(DEFAULT_EXPIRED_WEEKS),
        status = ApiKeyStatus.ACTIVE
      )
    }
  }

  /**
   * api key는 sha256으로 hash 되어있어 복호화 불가능
   */
  fun update(apiKey: String): ApiCredentials {
    val now = LocalDateTime.now()
    return this.copy(
      apiKey = apiKey,
      expiresAt = now.plusWeeks(DEFAULT_EXPIRED_WEEKS),
    )
  }
}

package com.ngyu.swiftpay.security.security.validator

import com.ngyu.swiftpay.core.domain.apiKey.ApiKey
import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.infrastructure.db.persistent.apiKey.ApiKeyRepository
import com.ngyu.swiftpay.security.util.HmacEncUtil
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ApiKeyValidator(
  private val apiKeyRepository: ApiKeyRepository
) {

  private val log = logger()

  fun verify(token: String, pairKey: String): Boolean {
    val apiKey = apiKeyRepository.findApiKey(pairKey)
    if (expiredToken(apiKey)) {
      log.warn("Token expired")
      return false
    }

    return HmacEncUtil.verify(token, apiKey.apiKey)
  }

  private fun expiredToken(apiKey: ApiKey): Boolean {
    val now = LocalDateTime.now()
    return now >= apiKey.expiresAt
  }

}

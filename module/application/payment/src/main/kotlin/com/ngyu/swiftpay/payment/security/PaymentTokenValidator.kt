package com.ngyu.swiftpay.payment.security

import com.ngyu.swiftpay.core.domain.apiCredentials.ApiCredentials
import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.port.ApiCredentialsRepository
import com.ngyu.swiftpay.infrastructure.redis.service.ApiKeyCacheService
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import com.ngyu.swiftpay.security.util.HmacEncUtil
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class PaymentTokenValidator(
  private val apiCredentialsRepository: ApiCredentialsRepository,
  private val apiKeyCacheService: ApiKeyCacheService
) {

  val log = logger()

  /**
   * API키 검증을 위한 메서드
   *
   * 1. Redis 캐시 확인
   *
   * 1-1. 캐시 미스 시 DB 조회
   *
   * 1-2. 검증 성공 시 Redis 캐싱
   *
   * 2. Redis 캐시에 저장된 키 만료 확인
   *
   * 3. 검증
   *
   * @param paymentCredentials 추출된 인증정보
   * @return Boolean
   */
  fun validate(paymentCredentials: PaymentCredentials): Boolean {
    val apiKey = paymentCredentials.apiKey
    val apiPairKey = paymentCredentials.apiPairKey

    val apiKeyData = apiKeyCacheService.find(apiPairKey) ?: run {
      val dbApiKey = apiCredentialsRepository.findApiKey(apiPairKey) ?: return false

      // 저장 전 토큰 만료 확인
      if(expiredToken(dbApiKey)) {
        log.warn("Expired API key")
        return false
      }

      apiKeyCacheService.save(dbApiKey)
      dbApiKey
    }

    // Redis에 저장된 토큰 만료 확인
    if(expiredToken(apiKeyData)) {
      log.warn("Expired API key")
      return false
    }

    return HmacEncUtil.verify(apiKey, apiKeyData.apiKey)
  }


  /**
   * 토큰 만료시간 검증
   * @param ApiKey 도메인
   * @return 만료됨 true, 유효함 false
   */
  private fun expiredToken(apiCredentials: ApiCredentials): Boolean {
    val now = LocalDateTime.now()
    return now >= apiCredentials.expiresAt
  }
}

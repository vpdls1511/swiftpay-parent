package com.ngyu.swiftpay.payment.security

import com.ngyu.swiftpay.core.domain.apiKey.ApiKey
import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.infrastructure.db.persistent.apiKey.ApiKeyRepository
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import com.ngyu.swiftpay.security.util.HmacEncUtil
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class PaymentTokenValidator(
  private val apiKeyRepository: ApiKeyRepository
) {

  val log = logger()

  /**
   * API키 검증을 위한 메서드
   * TODO - 1. Redis 캐시 확인
   * TODO - 2. 캐시 미스 시 DB 조회
   * TODO - 3. 검증 성공 시 Redis 캐싱
   *
   * @param paymentCredentials 추출된 인증정보
   * @return Boolean
   */
  fun validate(paymentCredentials: PaymentCredentials): Boolean {
    val apiKey = apiKeyRepository.findApiKey(paymentCredentials.apiPairKey)
    if (expiredToken(apiKey)) {
      log.warn("Token expired")
      return false
    }

    return HmacEncUtil.verify(paymentCredentials.apiKey, apiKey.apiKey)
  }


  /**
   * 토큰 만료시간 검증
   * @param ApiKey 도메인
   * @return 만료됨 true, 유효함 false
   */
  private fun expiredToken(apiKey: ApiKey): Boolean {
    val now = LocalDateTime.now()
    return now >= apiKey.expiresAt
  }
}

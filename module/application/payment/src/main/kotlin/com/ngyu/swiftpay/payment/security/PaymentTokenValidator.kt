package com.ngyu.swiftpay.payment.security

import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.port.repository.ApiCredentialsRepository
import com.ngyu.swiftpay.infrastructure.redis.service.ApiKeyCacheService
import com.ngyu.swiftpay.payment.api.controller.dto.PaymentCredentials
import com.ngyu.swiftpay.security.util.HmacEncUtil
import org.springframework.stereotype.Component

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

      apiKeyCacheService.save(dbApiKey)
      dbApiKey
    }

    return HmacEncUtil.verify(apiKey, apiKeyData.apiKey)
  }
}

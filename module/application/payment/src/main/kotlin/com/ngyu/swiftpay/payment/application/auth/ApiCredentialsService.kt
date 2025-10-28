package com.ngyu.swiftpay.payment.application.auth

import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.domain.apiCredentials.ApiCredentials
import com.ngyu.swiftpay.core.port.ApiCredentialsRepository
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import com.ngyu.swiftpay.security.provider.PaymentTokenProvider
import com.ngyu.swiftpay.security.vo.ApiKeyPair
import org.springframework.stereotype.Service

@Service
class ApiCredentialsService(
  private val paymentTokenProvider: PaymentTokenProvider,
  private val apiCredentialsRepository: ApiCredentialsRepository
) {

  private val log = logger()

  fun issueKey(): PaymentCredentials {
    log.info("API 키 발급 시작 - ")
    val pair: ApiKeyPair = paymentTokenProvider.issue()
    val apiCredentials: ApiCredentials = ApiCredentials.create(pair.hashed, pair.apiPairKey)

    log.debug("ApiKeyPair 발급 완료")
    log.debug("ApiKey Hash 값 db 저장")
    apiCredentialsRepository.save(apiCredentials)

    log.info("ApiKey 발급 & 저장 완료")
    return PaymentCredentials(
      apiKey = pair.plain,
      apiPairKey = pair.apiPairKey
    )
  }

}

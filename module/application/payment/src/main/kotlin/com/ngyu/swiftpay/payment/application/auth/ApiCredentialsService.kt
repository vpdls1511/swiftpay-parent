package com.ngyu.swiftpay.payment.application.auth

import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.domain.apiCredentials.ApiCredentials
import com.ngyu.swiftpay.core.port.repository.ApiCredentialsRepository
import com.ngyu.swiftpay.payment.api.controller.dto.PaymentCredentials
import com.ngyu.swiftpay.security.provider.PaymentTokenProvider
import com.ngyu.swiftpay.security.vo.ApiKeyPair
import org.springframework.stereotype.Service

@Service
class ApiCredentialsService(
  private val paymentTokenProvider: PaymentTokenProvider,
  private val apiCredentialsRepository: ApiCredentialsRepository
) {

  private val log = logger()

  fun issueKey(merchantId: Long): PaymentCredentials {
    log.info("API 키 발급 시작 - ")
    val pair: ApiKeyPair = paymentTokenProvider.issue()
    val apiCredentials: ApiCredentials = ApiCredentials.create(merchantId, pair.hashed, pair.apiPairKey)

    log.debug("ApiKeyPair 발급 완료")
    log.debug("ApiKey Hash 값 db 저장")
    apiCredentialsRepository.save(apiCredentials)

    log.info("ApiKey 발급 & 저장 완료")
    return PaymentCredentials(
      apiKey = pair.plain,
      apiPairKey = pair.apiPairKey
    )
  }

  fun reissueToken(merchantId: Long, apiPairKey: String): PaymentCredentials {
    log.info("API키 재발행 시작")
    val existApiCredentials = apiCredentialsRepository.findApiKey(apiPairKey)
    val pair = paymentTokenProvider.issue()
    val apiCredentials: ApiCredentials = ApiCredentials.create(merchantId, pair.hashed, existApiCredentials.lookupKey)

    val updateCredentials = existApiCredentials.update(apiCredentials.apiKey)

    log.debug("new ApiKey Hash 값 db 저장")
    apiCredentialsRepository.save(updateCredentials)

    return PaymentCredentials(
      apiKey = pair.plain,
      apiPairKey = apiPairKey
    )
  }

}

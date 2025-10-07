package com.ngyu.swiftpay.payment.api.application.service

import com.ngyu.swiftpay.core.domain.apiKey.ApiKey
import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.infrastructure.db.persistent.apiKey.ApiKeyRepository
import com.ngyu.swiftpay.payment.api.application.usecase.PaymentApiKeyUseCase
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import com.ngyu.swiftpay.security.provider.PaymentTokenProvider
import com.ngyu.swiftpay.security.vo.ApiKeyPair
import org.springframework.stereotype.Service

@Service
class PaymentApiKeyService(
  private val paymentTokenProvider: PaymentTokenProvider,
  private val apiKeyRepository: ApiKeyRepository
) : PaymentApiKeyUseCase {

  private val log = logger()

  override fun issueKey(): PaymentCredentials {
    log.info("API 키 발급 시작 - ")
    val pair: ApiKeyPair = paymentTokenProvider.issue()
    val apiKey: ApiKey = ApiKey.create(pair.hashed, pair.lookupKey)

    log.debug("ApiKeyPair 발급 완료")
    log.debug("ApiKey Hash 값 db 저장")
    apiKeyRepository.save(apiKey)

    log.info("ApiKey 발급 & 저장 완료")
    return PaymentCredentials(
      apiKey = pair.plain,
      apiPairKey = pair.lookupKey
    )
  }

}

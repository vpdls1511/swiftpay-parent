package com.ngyu.swiftpay.payment.api.application

import com.ngyu.swiftpay.core.domain.apiKey.ApiKey
import com.ngyu.swiftpay.infrastructure.db.persistent.apiKey.ApiKeyRepository
import com.ngyu.swiftpay.payment.api.dto.ApiKeyResponse
import com.ngyu.swiftpay.security.provider.PaymentTokenProvider
import com.ngyu.swiftpay.security.vo.ApiKeyPair
import org.springframework.stereotype.Service

@Service
class PaymentApiKeyService(
  private val paymentTokenProvider: PaymentTokenProvider,
  private val apiKeyRepository: ApiKeyRepository
) : PaymentApiKeyUseCase {
  override fun issueKey(): ApiKeyResponse {
    val pair: ApiKeyPair = paymentTokenProvider.issue()
    val apiKey: ApiKey = ApiKey.create(pair.hashed)

    apiKeyRepository.save(apiKey)

    return ApiKeyResponse(pair.plain)
  }
}

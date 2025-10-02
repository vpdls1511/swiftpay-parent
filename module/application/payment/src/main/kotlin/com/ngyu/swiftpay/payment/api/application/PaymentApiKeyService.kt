package com.ngyu.swiftpay.payment.api.application

import com.ngyu.swiftpay.core.domain.apiKey.ApiKey
import com.ngyu.swiftpay.infrastructure.db.persistent.apiKey.ApiKeyEntity
import com.ngyu.swiftpay.infrastructure.db.persistent.apiKey.ApiKeyMapper
import com.ngyu.swiftpay.infrastructure.db.persistent.apiKey.ApiKeyRepositoryAdapter
import com.ngyu.swiftpay.payment.api.dto.ApiKeyResponse
import com.ngyu.swiftpay.security.provider.PaymentTokenProvider
import org.springframework.stereotype.Service

@Service
class PaymentApiKeyService(
  private val paymentTokenProvider: PaymentTokenProvider,
  private val apiKeyRepositoryAdapter: ApiKeyRepositoryAdapter
) : PaymentApiKeyUseCase {
  override fun issueKey(): ApiKeyResponse {
    val key: String = paymentTokenProvider.issue()
    val apiKey: ApiKey = ApiKey.create(key)

    val apiKeyEntity: ApiKeyEntity = ApiKeyMapper.toEntity(apiKey)
    apiKeyRepositoryAdapter.save(apiKeyEntity)

    return ApiKeyResponse(key)
  }
}

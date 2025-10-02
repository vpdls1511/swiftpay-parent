package com.ngyu.swiftpay.infrastructure.db.persistent.apiKey

import org.springframework.stereotype.Component

@Component
class ApiKeyRepositoryAdapter(
  private val apiKeyRepository: ApiKeyRepository
) {

  fun save(entity: ApiKeyEntity): ApiKeyEntity {
    return apiKeyRepository.save(entity)
  }
}

package com.ngyu.swiftpay.infrastructure.db.persistent.apiKey

import com.ngyu.swiftpay.core.domain.apiKey.ApiKey
import com.ngyu.swiftpay.core.domain.apiKey.ApiKeyRepository
import org.springframework.stereotype.Component

@Component
class ApiKeyRepositoryAdapter(
  private val jpaRepository: ApiKeyJpaRepository
): ApiKeyRepository {

  override fun save(domain: ApiKey): ApiKey {
    val entity: ApiKeyEntity = ApiKeyMapper.toEntity(domain)
    val savedEntity = jpaRepository.save(entity)

    return ApiKeyMapper.toDomain(savedEntity)
  }

  override fun findApiKey(lookupKey: String): ApiKey {
    val entity = jpaRepository.findByLookupKey(lookupKey)
      ?: throw Exception("Entity not found")

    return ApiKeyMapper.toDomain(entity)
  }
}

package com.ngyu.swiftpay.infrastructure.db.persistent.apiCredentials

import com.ngyu.swiftpay.core.domain.apiCredentials.ApiCredentials
import com.ngyu.swiftpay.core.port.repository.ApiCredentialsRepository
import org.springframework.stereotype.Component

@Component
class ApiCredentialsRepositoryAdapter(
  private val jpaRepository: ApiCredentialsJpaRepository
): ApiCredentialsRepository {

  override fun save(domain: ApiCredentials): ApiCredentials {
    val entity: ApiCredentialsEntity = ApiCredentialsMapper.toEntity(domain)
    val savedEntity = jpaRepository.save(entity)

    return ApiCredentialsMapper.toDomain(savedEntity)
  }

  override fun findApiKey(lookupKey: String): ApiCredentials {
    val entity = jpaRepository.findByLookupKey(lookupKey)
      ?: throw Exception("Entity not found")

    return ApiCredentialsMapper.toDomain(entity)
  }
}

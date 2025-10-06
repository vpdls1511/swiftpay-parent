package com.ngyu.swiftpay.infrastructure.db.persistent.apiKey

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApiKeyJpaRepository: JpaRepository<ApiKeyEntity, String> {
  fun findByLookupKey(lookupKey: String): ApiKeyEntity?
}

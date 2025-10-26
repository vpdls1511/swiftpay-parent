package com.ngyu.swiftpay.infrastructure.db.persistent.apiCredentials

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApiCredentialsJpaRepository: JpaRepository<ApiCredentialsEntity, String> {
  fun findByLookupKey(lookupKey: String): ApiCredentialsEntity?
}

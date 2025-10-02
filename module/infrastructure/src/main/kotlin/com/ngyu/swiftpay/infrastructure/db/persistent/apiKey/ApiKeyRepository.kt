package com.ngyu.swiftpay.infrastructure.db.persistent.apiKey

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApiKeyRepository: JpaRepository<ApiKeyEntity, String> {
}

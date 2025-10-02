package com.ngyu.swiftpay.infrastructure.db.persistent.apiKey

import com.ngyu.swiftpay.core.domain.apiKey.ApiKey

object ApiKeyMapper {

  fun toEntity(domain: ApiKey): ApiKeyEntity {
    return ApiKeyEntity(
      apiKey = domain.apiKey,
      userId = domain.userId,
      callLimit = domain.callLimit,
      issuedAt = domain.issuedAt,
      expiresAt = domain.expiresAt,
      status = domain.status,
    )
  }

  fun toDomain(entity: ApiKeyEntity): ApiKey {
    return ApiKey(
      apiKey = entity.apiKey,
      userId = entity.userId,
      callLimit = entity.callLimit,
      issuedAt = entity.issuedAt,
      expiresAt = entity.expiresAt,
      status = entity.status,
    )
  }
}

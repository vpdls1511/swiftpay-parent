package com.ngyu.swiftpay.infrastructure.db.persistent.apiCredentials

import com.ngyu.swiftpay.core.domain.apiCredentials.ApiCredentials

object ApiCredentialsMapper {

  fun toEntity(domain: ApiCredentials): ApiCredentialsEntity {
    return ApiCredentialsEntity(
      apiKey = domain.apiKey,
      lookupKey = domain.lookupKey,
      userId = domain.userId,
      callLimit = domain.callLimit,
      issuedAt = domain.issuedAt,
      expiresAt = domain.expiresAt,
      status = domain.status,
    )
  }

  fun toDomain(entity: ApiCredentialsEntity): ApiCredentials {
    return ApiCredentials(
      apiKey = entity.apiKey,
      lookupKey = entity.lookupKey,
      userId = entity.userId,
      callLimit = entity.callLimit,
      issuedAt = entity.issuedAt,
      expiresAt = entity.expiresAt,
      status = entity.status,
    )
  }
}

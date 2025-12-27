package com.ngyu.swiftpay.infrastructure.db.persistent.apiCredentials

import com.ngyu.swiftpay.core.domain.apiCredentials.ApiKeyStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "api_credentials")
class ApiCredentialsEntity(
  @Id
  @Column(name = "merchant_id")
  val merchantId: Long,

  @Column(name = "api_key", unique = true, nullable = false)
  val apiKey: String,

  @Column(name = "lookup_key", unique = true, nullable = false)
  val lookupKey: String,

  @Column(name = "call_limit", nullable = false)
  val callLimit: Int,

  @Column(name = "issued_at", nullable = false)
  val issuedAt: LocalDateTime,

  @Column(name = "expires_at", nullable = false)
  val expiresAt: LocalDateTime,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  val status: ApiKeyStatus
) {
  protected constructor() : this(
    apiKey = "",
    lookupKey = "",
    merchantId = 0,
    callLimit = 0,
    issuedAt = LocalDateTime.now(),
    expiresAt = LocalDateTime.now(),
    status = ApiKeyStatus.ACTIVE
  )
}

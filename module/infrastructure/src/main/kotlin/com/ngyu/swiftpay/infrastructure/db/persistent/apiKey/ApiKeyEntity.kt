package com.ngyu.swiftpay.infrastructure.db.persistent.apiKey

import com.ngyu.swiftpay.core.domain.apiKey.ApiKeyStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "api_key")
class ApiKeyEntity(
  @Id
  @Column(name = "api_key", unique = true, nullable = false)
  val apiKey: String,

  @Column(name = "lookup_key", unique = true, nullable = false)
  val lookupKey: String,

  @Column(name = "user_id")
  val userId: Long? = null,

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
    userId = null,
    callLimit = 0,
    issuedAt = LocalDateTime.now(),
    expiresAt = LocalDateTime.now(),
    status = ApiKeyStatus.ACTIVE
  )
}

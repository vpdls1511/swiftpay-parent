package com.ngyu.swiftpay.infrastructure.redis.service

import com.ngyu.swiftpay.core.common.exception.PrincipalException
import com.ngyu.swiftpay.core.domain.apiCredentials.ApiCredentials
import com.ngyu.swiftpay.core.domain.apiCredentials.ApiKeyStatus
import com.ngyu.swiftpay.infrastructure.redis.constant.RedisKey
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class ApiKeyCacheService(
  private val redisTemplate: StringRedisTemplate,
) {
  private val hashOps = redisTemplate.opsForHash<String, String>()

  fun save(apiCredentials: ApiCredentials) {
    val key = RedisKey.apiKey(apiCredentials.lookupKey)

    val map = mapOf(
      "merchantId" to apiCredentials.merchantId.toString(),  // 이렇게 수정
      "apiKey" to apiCredentials.apiKey,
      "lookupKey" to apiCredentials.lookupKey,
      "issuedAt" to apiCredentials.issuedAt.toString(),
      "expiresAt" to apiCredentials.expiresAt.toString(),
      "status" to apiCredentials.status.toString(),
    )

    hashOps.putAll(key, map)

    val duration = Duration.between(LocalDateTime.now(), apiCredentials.expiresAt).coerceAtLeast(Duration.ZERO)
    redisTemplate.expire(key, duration)
  }

  fun find(apiPairKey: String): ApiCredentials? {
    val key = RedisKey.apiKey(apiPairKey)
    val entries = hashOps.entries(key)

    return if (entries.isEmpty()) null else entries.toDomain()
  }

  // TODO - 가능성은 미미하나, 만약 RaceCondition 발생 시 HINCRBY 적용하기
  fun decreaseCallLimit(apiPairKey: String): Int? {
    val key = RedisKey.apiKey(apiPairKey)
    val callLimit = hashOps.get(key, "callLimit")?.toIntOrNull() ?: return null
    val newCallLimit = callLimit - 1

    hashOps.put(key, "callLimit", newCallLimit.toString())

    return newCallLimit
  }

  fun delete(apiPairKey: String) {
    val key = RedisKey.apiKey(apiPairKey)
    redisTemplate.delete(key)
  }

  private fun Map<String, String>.toDomain(): ApiCredentials {
    return ApiCredentials(
      apiKey = this["apiKey"] ?: throw PrincipalException("apiKey가 없습니다."),
      lookupKey = this["lookupKey"] ?: throw PrincipalException("lookupKey가 없습니다."),
      merchantId = this["merchantId"]?.toLongOrNull() ?: 0,
      callLimit = this["callLimit"]?.toIntOrNull() ?: 0,
      issuedAt = this["issuedAt"]?.let { LocalDateTime.parse(it) }
        ?: throw PrincipalException("issuedAt이 없습니다."),
      expiresAt = this["expiresAt"]?.let { LocalDateTime.parse(it) }
        ?: throw PrincipalException("expiresAt이 없습니다."),
      status = runCatching { ApiKeyStatus.valueOf(this["status"] ?: "INACTIVE") }
        .getOrElse { ApiKeyStatus.INACTIVE }
    )
  }
}

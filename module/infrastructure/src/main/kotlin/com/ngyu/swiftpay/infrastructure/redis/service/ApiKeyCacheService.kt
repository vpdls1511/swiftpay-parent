package com.ngyu.swiftpay.infrastructure.redis.service

import com.ngyu.swiftpay.core.domain.apiKey.ApiKey
import com.ngyu.swiftpay.core.domain.apiKey.ApiKeyStatus
import com.ngyu.swiftpay.core.exception.PrincipalException
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

  fun save(apiKey: ApiKey) {
    val key = RedisKey.apiKey(apiKey.lookupKey)

    val map = mapOf(
      "apiKey" to apiKey.apiKey,
      "lookupKey" to apiKey.lookupKey,
      "userId" to (apiKey.userId.toString() ?: ""),
      "issuedAt" to apiKey.issuedAt.toString(),
      "expiresAt" to apiKey.expiresAt.toString(),
      "status" to apiKey.status.toString(),
    )

    hashOps.putAll(key, map)

    val duration = Duration.between(LocalDateTime.now(), apiKey.expiresAt).coerceAtLeast(Duration.ZERO)
    redisTemplate.expire(key, duration)
  }

  fun find(apiPairKey: String): ApiKey? {
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

  private fun Map<String, String>.toDomain(): ApiKey {
    return ApiKey(
      apiKey = this["apiKey"] ?: throw PrincipalException("apiKey가 없습니다."),
      lookupKey = this["lookupKey"] ?: throw PrincipalException("lookupKey가 없습니다."),
      userId = this["userId"]?.toLong(),
      callLimit = this["callLimit"]?.toIntOrNull() ?: 0,
      issuedAt = this["issuedAt"]?.let { LocalDateTime.parse(it) }
        ?: throw PrincipalException("issuedAt이 없습니다."),
      expiresAt = this["expiresAt"]?.let { LocalDateTime.parse(it) }
        ?: throw PrincipalException("expiresAt이 없습니다."),
      status = (this["status"] ?: ApiKeyStatus.INACTIVE) as ApiKeyStatus,
    )
  }
}

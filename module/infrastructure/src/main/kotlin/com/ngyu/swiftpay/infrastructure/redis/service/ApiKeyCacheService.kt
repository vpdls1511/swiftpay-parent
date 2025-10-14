package com.ngyu.swiftpay.infrastructure.redis.service

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

  fun save(apiKey: String,
           apiPairKey: String,
           expiredAt: LocalDateTime,
           callLimit: Int
           ) {
    val key = RedisKey.apiKey(apiPairKey)

    val map = mapOf(
      "apiKey" to apiKey,
      "expiredAt" to expiredAt.toString(),
      "callLimit" to callLimit.toString(),
    )
    val duration = Duration.between(LocalDateTime.now(), expiredAt).coerceAtLeast(Duration.ZERO)

    hashOps.putAll(key, map)
    redisTemplate.expire(key, duration)
  }

  fun find(apiPairKey: String): Map<String, String>? {
    val key = RedisKey.apiKey(apiPairKey)
    val entries = hashOps.entries(key)

    return if (entries.isEmpty()) null else entries
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
}

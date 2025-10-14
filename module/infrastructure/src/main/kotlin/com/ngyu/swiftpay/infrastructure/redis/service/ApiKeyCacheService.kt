package com.ngyu.swiftpay.infrastructure.redis.service

import com.ngyu.swiftpay.infrastructure.redis.constant.RedisKey
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class ApiKeyCacheService(
  private val redisTemplate: StringRedisTemplate,
) {
  fun save(apiKey: String, apiPairKey: String) {
    val key = RedisKey.apiKey(apiPairKey)
    redisTemplate.opsForValue().set(key, apiKey, Duration.ofHours(1))
  }

  fun find(apiPairKey: String): String? {
    val key = RedisKey.apiKey(apiPairKey)
    return redisTemplate.opsForValue().get(key)
  }

  fun delete(apiPairKey: String) {
    val key = RedisKey.apiKey(apiPairKey)
    redisTemplate.delete(key)
  }
}

package me.ngyu.swiftpay.common.redis.constant

object RedisKeyPrefix {
  const val API_KEY: String = "swiftpay:api:key"
}

object RedisKey {
  fun apiKey(apiKeyPair: String) = "${RedisKeyPrefix.API_KEY}:$apiKeyPair"
}

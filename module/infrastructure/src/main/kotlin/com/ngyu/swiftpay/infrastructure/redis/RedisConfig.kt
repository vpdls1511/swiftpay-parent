package com.ngyu.swiftpay.infrastructure.redis

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
class RedisConfig {
  lateinit var host: String
  var port: Int = 6379
  var password: String? = null

  @Bean
  fun redisConnectionFactory(): RedisConnectionFactory {
    val config = RedisStandaloneConfiguration(host, port)
    password?.takeIf { it.isNotBlank() }?.let { config.setPassword(it) }
    return LettuceConnectionFactory(config)
  }

  @Bean
  fun stringRedisTemplate(connectionFactory: RedisConnectionFactory): StringRedisTemplate {
    return StringRedisTemplate(connectionFactory)
  }
}

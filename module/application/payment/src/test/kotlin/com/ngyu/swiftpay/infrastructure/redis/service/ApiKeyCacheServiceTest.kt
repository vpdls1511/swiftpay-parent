package com.ngyu.swiftpay.infrastructure.redis.service

import com.ngyu.swiftpay.core.domain.apiCredentials.ApiCredentials
import com.ngyu.swiftpay.core.domain.apiCredentials.ApiKeyStatus
import com.ngyu.swiftpay.core.common.exception.PrincipalException
import com.ngyu.swiftpay.infrastructure.redis.constant.RedisKey
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.LocalDateTime

@DisplayName("ApiKeyCacheService 단위 테스트")
class ApiCredentialsCacheServiceUnitTest {

 private val redisTemplate = mock(StringRedisTemplate::class.java)
 private val hashOps = mock(HashOperations::class.java) as HashOperations<String, String, String>

 private val service: ApiKeyCacheService

 init {
  `when`(redisTemplate.opsForHash<String, String>()).thenReturn(hashOps)
  service = ApiKeyCacheService(redisTemplate)
 }

 @Test
 @DisplayName("find() - Redis에 데이터가 존재하면 ApiKey 도메인으로 변환되어 반환된다")
 fun find_whenDataExists_shouldReturnApiKeyDomain() {
  // given
  val key = RedisKey.apiKey("pair-1234")
  val entries = mapOf(
   "apiKey" to "api-key-xyz",
   "lookupKey" to "pair-1234",
   "userId" to "1001",
   "callLimit" to "10",
   "issuedAt" to LocalDateTime.now().minusHours(1).toString(),
   "expiresAt" to LocalDateTime.now().plusHours(2).toString(),
   "status" to ApiKeyStatus.ACTIVE.toString()
  )

  `when`(hashOps.entries(key)).thenReturn(entries)

  // when
  val result = service.find("pair-1234")

  // then
  assertThat(result).isNotNull
  assertThat(result!!.apiKey).isEqualTo("api-key-xyz")
  assertThat(result.lookupKey).isEqualTo("pair-1234")
  assertThat(result.userId).isEqualTo(1001L)
  assertThat(result.status).isEqualTo(ApiKeyStatus.ACTIVE)
 }

 @Test
 @DisplayName("find() - Redis에 데이터가 존재하지 않으면 null을 반환한다")
 fun find_whenNoData_shouldReturnNull() {
  // given
  val key = RedisKey.apiKey("pair-9999")
  `when`(hashOps.entries(key)).thenReturn(emptyMap())

  // when
  val result = service.find("pair-9999")

  // then
  assertThat(result).isNull()
 }

 @Test
 @DisplayName("find() - 필수 필드(apiKey, issuedAt 등)가 누락되면 PrincipalException을 발생시킨다")
 fun find_whenRequiredFieldsMissing_shouldThrowPrincipalException() {
  // given
  val key = RedisKey.apiKey("pair-0001")
  val incompleteEntries = mapOf("lookupKey" to "pair-0001")

  `when`(hashOps.entries(key)).thenReturn(incompleteEntries)

  // expect
  assertThrows(PrincipalException::class.java) {
   service.find("pair-0001")
  }
 }

 @Test
 @DisplayName("save() - ApiKey를 Redis Hash에 저장할 때 putAll과 expire가 호출된다")
 fun save_shouldCallPutAllAndExpire() {
  // given
  val apiCredentials = ApiCredentials(
   apiKey = "api-key-1234",
   lookupKey = "pair-1234",
   userId = 1001L,
   callLimit = 10,
   issuedAt = LocalDateTime.now(),
   expiresAt = LocalDateTime.now().plusHours(1),
   status = ApiKeyStatus.ACTIVE
  )

  // when
  service.save(apiCredentials)

  // then
  verify(hashOps, times(1)).putAll(eq(RedisKey.apiKey("pair-1234")), anyMap())
  verify(redisTemplate, times(1)).expire(anyString(), any())
 }

 @Test
 @DisplayName("decreaseCallLimit() - callLimit을 1 감소시켜 Redis에 반영한다")
 fun decreaseCallLimit_shouldDecreaseCallLimitByOne() {
  // given
  val key = RedisKey.apiKey("pair-1234")
  `when`(hashOps.get(key, "callLimit")).thenReturn("10")

  // when
  val result = service.decreaseCallLimit("pair-1234")

  // then
  assertThat(result).isEqualTo(9)
  verify(hashOps).put(eq(key), eq("callLimit"), eq("9"))
 }

 @Test
 @DisplayName("delete() - Redis에서 해당 key를 삭제한다")
 fun delete_shouldRemoveKeyFromRedis() {
  // given
  val lookupKey = "pair-5678"

  // when
  service.delete(lookupKey)

  // then
  verify(redisTemplate, times(1)).delete(RedisKey.apiKey(lookupKey))
 }
}

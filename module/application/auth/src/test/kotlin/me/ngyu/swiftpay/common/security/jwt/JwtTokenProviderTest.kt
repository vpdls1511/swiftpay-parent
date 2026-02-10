package me.ngyu.swiftpay.common.security.jwt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class JwtTokenProviderTest {

  private lateinit var tokenProvider: JwtTokenProvider

  @BeforeEach
  fun setUp() {
    val properties = JwtProperties(
      secret = "test-secret-key-must-be-at-least-32-bytes-long!!",
      accessExpiration = 1800000,   // 30분
      refreshExpiration = 604800000  // 7일
    )
    tokenProvider = JwtTokenProvider(properties)
  }

  @Test
  fun `accessToken 생성 및 파싱`() {
    // given
    val uuid = "test-uuid-1234"
    val userId = 1L
    val role = "USER"

    // when
    val token = tokenProvider.createAccessToken(uuid, userId,role)
    val payload = tokenProvider.parseToken(token)

    // then
    assertThat(payload.role).isEqualTo(role)
  }

  @Test
  fun `refreshToken은 role이 null`() {
    // given
    val uuid = "test-uuid-1234"

    // when
    val token = tokenProvider.createRefreshToken(uuid)
    val payload = tokenProvider.parseToken(token)

    // then
    assertThat(payload.role).isNull()
  }

  @Test
  fun `잘못된 토큰 파싱 시 예외 발생`() {
    assertThrows<Exception> {
      tokenProvider.parseToken("invalid.token.here")
    }
  }

  @Test
  fun `만료된 토큰 파싱 시 예외 발생`() {
    // given
    val properties = JwtProperties(
      secret = "test-secret-key-must-be-at-least-32-bytes-long!!",
      accessExpiration = -1000,  // 이미 만료
      refreshExpiration = -1000
    )
    val expiredProvider = JwtTokenProvider(properties)

    val token = expiredProvider.createAccessToken("uuid", 1L, "USER")

    // when & then
    assertThrows<Exception> {
      tokenProvider.parseToken(token)
    }
  }
}

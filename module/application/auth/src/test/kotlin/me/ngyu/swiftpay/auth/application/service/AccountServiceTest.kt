package me.ngyu.swiftpay.auth.application.service

import com.ngyu.swiftpay.core.exception.SwiftException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import me.ngyu.swiftpay.auth.api.dto.AccountRequest
import me.ngyu.swiftpay.auth.api.dto.LoginRequest
import me.ngyu.swiftpay.auth.domain.account.Account
import me.ngyu.swiftpay.auth.domain.account.AccountCredentials
import me.ngyu.swiftpay.auth.domain.account.AccountProfile
import me.ngyu.swiftpay.auth.domain.account.port.AccountRepositoryPort
import me.ngyu.swiftpay.common.security.jwt.JwtTokenProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class AccountServiceTest {

  @MockK
  private lateinit var accountRepositoryPort: AccountRepositoryPort

  @MockK
  private lateinit var passwordEncoder: PasswordEncoder

  @MockK
  private lateinit var tokenProvider: JwtTokenProvider

  @InjectMockKs
  private lateinit var accountService: AccountService

  @Test
  fun `회원가입_성공`() {
    // given
    val request = AccountRequest("user", "pass", "홍길동", "test@test.com", "010-1234-5678")
    val account = Account()

    every { accountRepositoryPort.existsByUsername(any()) } returns false
    every { accountRepositoryPort.existsByEmail(any()) } returns false
    every { passwordEncoder.encode(any()) } returns "encodedPass"
    every { accountRepositoryPort.save(any()) } returns account
    every { accountRepositoryPort.saveCredentials(any()) } returns mockk<AccountCredentials>()
    every { accountRepositoryPort.saveProfile(any()) } returns mockk<AccountProfile>()

    // when
    val result = accountService.saveUser(request)

    // then
    assertThat(result).isEqualTo(account.uuid)
    verify(exactly = 1) { accountRepositoryPort.save(any()) }
  }

  @Test
  fun `중복 username 예외 발생`() {
    // given
    val request = AccountRequest("user", "pass", "홍길동", "test@test.com", "010-1234-5678")
    every { accountRepositoryPort.existsByUsername("user") } returns true

    // when & then
    assertThrows<SwiftException> {
      accountService.saveUser(request)
    }
  }

  @Test
  fun `중복 email 예외 발생`() {
    // given
    val request = AccountRequest("user", "pass", "홍길동", "test@test.com", "010-1234-5678")

    every { accountRepositoryPort.existsByUsername("user") } returns false  // username은 통과
    every { accountRepositoryPort.existsByEmail("test@test.com") } returns true  // email 중복

    // when & then
    assertThrows<SwiftException> {
      accountService.saveUser(request)
    }
  }

  @Test
  fun `로그인_성공`() {
    // given
    val request = LoginRequest("user", "pass")
    val account = Account()
    val credentials = AccountCredentials(
      account = account,
      username = "user",
      password = "encodedPass"
    )

    every { accountRepositoryPort.findByUsername("user") } returns credentials
    every { passwordEncoder.matches("pass", "encodedPass") } returns true
    every { tokenProvider.createAccessToken(account.uuid, account.id, account.role.name) } returns "accessToken"
    every { tokenProvider.createRefreshToken(account.uuid) } returns "refreshToken"

    // when
    val result = accountService.login(request)

    // then
    assertThat(result.accessToken).isEqualTo("accessToken")
    assertThat(result.refreshToken).isEqualTo("refreshToken")
  }

  @Test
  fun `로그인_비밀번호_불일치_예외`() {
    // given
    val request = LoginRequest("user", "wrongPass")
    val credentials = AccountCredentials(
      account = Account(),
      username = "user",
      password = "encodedPass"
    )

    every { accountRepositoryPort.findByUsername("user") } returns credentials
    every { passwordEncoder.matches("wrongPass", "encodedPass") } returns false

    // when & then
    assertThrows<SwiftException> {
      accountService.login(request)
    }
  }

}

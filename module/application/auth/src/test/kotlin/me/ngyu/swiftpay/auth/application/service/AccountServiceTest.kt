package me.ngyu.swiftpay.auth.application.service

import com.ngyu.swiftpay.core.exception.SwiftException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import me.ngyu.swiftpay.auth.application.api.dto.AccountRequest
import me.ngyu.swiftpay.auth.domain.account.Account
import me.ngyu.swiftpay.auth.domain.account.AccountCredentials
import me.ngyu.swiftpay.auth.domain.account.AccountProfile
import me.ngyu.swiftpay.auth.domain.account.port.AccountRepositoryPort
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
}

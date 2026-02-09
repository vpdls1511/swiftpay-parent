package me.ngyu.swiftpay.auth.infrastructure.adapter

import com.ngyu.swiftpay.core.exception.SwiftException
import me.ngyu.swiftpay.auth.domain.account.Account
import me.ngyu.swiftpay.auth.domain.account.AccountCredentials
import me.ngyu.swiftpay.auth.domain.account.AccountRole
import me.ngyu.swiftpay.auth.domain.account.AccountStatus
import me.ngyu.swiftpay.auth.domain.account.port.AccountRepositoryPort
import me.ngyu.swiftpay.auth.infrastructure.persistent.AccountJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.util.*

@DataJpaTest
@ActiveProfiles("test")
@Import(AccountRepositoryAdapter::class)
class AccountRepositoryAdapterTest {

  @Autowired
  private lateinit var accountRepositoryAdapter: AccountRepositoryPort

  @Autowired
  private lateinit var accountJpaRepository: AccountJpaRepository

  private lateinit var testAccount: Account

  @BeforeEach
  fun setup() {
    accountJpaRepository.deleteAll()

    testAccount = Account(
      uuid = UUID.randomUUID().toString(),
      role = AccountRole.USER,
      status = AccountStatus.ACTIVE
    )
  }

  @Test
  fun `Account 저장 성공`() {
    // When
    val saved = accountRepositoryAdapter.save(testAccount)

    // Then
    assertThat(saved.id).isNotNull()
    assertThat(saved.uuid).isEqualTo(testAccount.uuid)
    assertThat(saved.role).isEqualTo(AccountRole.USER)
  }

  @Test
  fun `ID로 Account 조회 성공`() {
    // Given
    val saved = accountJpaRepository.save(testAccount)

    // When
    val found = accountRepositoryAdapter.findById(saved.id)

    // Then
    assertThat(found.id).isEqualTo(saved.id)
    assertThat(found.uuid).isEqualTo(saved.uuid)
  }

  @Test
  fun `존재하지 않는 ID로 조회 시 예외 발생`() {
    // When & Then
    assertThrows<SwiftException> {
      accountRepositoryAdapter.findById(999L)
    }
  }

  @Test
  fun `UUID로 Account 조회 성공`() {
    // Given
    val saved = accountJpaRepository.save(testAccount)

    // When
    val found = accountRepositoryAdapter.findByUuid(saved.uuid)

    // Then
    assertThat(found.uuid).isEqualTo(saved.uuid)
  }

  @Test
  fun `username 존재 여부 확인`() {
    // Given
    val saved = accountJpaRepository.save(testAccount)
    val credentials = AccountCredentials(
      account = saved,
      username = "testuser",
      password = "hashed-password"
    )
    accountRepositoryAdapter.saveCredentials(credentials)

    // When
    val exists = accountRepositoryAdapter.existsByUsername("testuser")

    // Then
    assertThat(exists).isTrue()
  }

  @Test
  fun `존재하지 않는 username 확인`() {
    // When
    val exists = accountRepositoryAdapter.existsByUsername("nonexistent")

    // Then
    assertThat(exists).isFalse()
  }
}

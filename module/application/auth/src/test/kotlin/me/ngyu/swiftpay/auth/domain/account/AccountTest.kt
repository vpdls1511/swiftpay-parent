package me.ngyu.swiftpay.auth.domain.account

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.LocalDateTime
import java.util.*

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountTest {

  @Autowired
  private lateinit var em: TestEntityManager

  @Test
  @DisplayName("계정 생성 및 저장")
  fun createAccount() {
    // given
    val account = Account(
      uuid = UUID.randomUUID().toString(),
      role = AccountRole.USER,
      status = AccountStatus.ACTIVE
    )

    // when
    val saved = em.persistAndFlush(account)

    // then
    assertThat(saved.id).isNotNull()
    assertThat(saved.uuid).isNotBlank()
    assertThat(saved.role).isEqualTo(AccountRole.USER)
  }

  @Test
  @DisplayName("도메인 로직 - 역할 변경")
  fun changeRole() {
    // given
    val account = Account(
      uuid = UUID.randomUUID().toString(),
      role = AccountRole.USER,
      status = AccountStatus.ACTIVE
    )

    // when
    account.changeRole(AccountRole.MERCHANT)

    // then
    assertThat(account.role).isEqualTo(AccountRole.MERCHANT)
  }

  @Test
  @DisplayName("도메인 로직 - 상태 변경")
  fun changeStatus() {
    // given
    val account = Account(
      uuid = UUID.randomUUID().toString(),
      role = AccountRole.USER,
      status = AccountStatus.ACTIVE
    )

    // when & then
    account.suspend()
    assertThat(account.status).isEqualTo(AccountStatus.SUSPENDED)

    account.activate()
    assertThat(account.status).isEqualTo(AccountStatus.ACTIVE)

    account.delete()
    assertThat(account.status).isEqualTo(AccountStatus.DELETED)
  }

  @Test
  @DisplayName("계정 + 인증정보 + 프로필 통합 저장")
  fun saveAccountWithRelations() {
    // given
    val account = Account(
      uuid = UUID.randomUUID().toString(),
      role = AccountRole.USER,
      status = AccountStatus.ACTIVE
    )
    em.persistAndFlush(account)

    val credentials = AccountCredentials(
      account = account,
      username = "testuser",
      password = "hashed_password",
      passwordChangedAt = LocalDateTime.now()
    )

    val profile = AccountProfile(
      account = account,
      name = "홍길동",
      email = "test@example.com",
      phone = "01012345678"
    )

    // when
    em.persist(credentials)
    em.persist(profile)
    em.flush()
    em.clear()

    // then
    val foundCredentials = em.find(AccountCredentials::class.java, account.id)
    val foundProfile = em.find(AccountProfile::class.java, account.id)

    assertThat(foundCredentials).isNotNull
    assertThat(foundCredentials.username).isEqualTo("testuser")
    assertThat(foundCredentials.account.uuid).isEqualTo(account.uuid)

    assertThat(foundProfile).isNotNull
    assertThat(foundProfile.name).isEqualTo("홍길동")
    assertThat(foundProfile.account.uuid).isEqualTo(account.uuid)
  }
}

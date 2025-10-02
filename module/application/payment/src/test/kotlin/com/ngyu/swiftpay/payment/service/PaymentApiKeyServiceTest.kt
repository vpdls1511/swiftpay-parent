package com.ngyu.swiftpay.payment.service

import com.ngyu.swiftpay.core.domain.apiKey.ApiKey
import com.ngyu.swiftpay.infrastructure.db.persistent.apiKey.ApiKeyEntity
import com.ngyu.swiftpay.security.fake.FakeApiKeyJpaRepository
import com.ngyu.swiftpay.security.provider.PaymentTokenProvider
import com.ngyu.swiftpay.security.vo.ApiKeyPair
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PaymentApiKeyServiceTest {

  private val SECRET_KEY: String = "ck7488ZlcflJ+ZwsY/h9vIzPRjS5KsuAlQS9lBVBgks="
  private val paymentTokenProvider: PaymentTokenProvider = PaymentTokenProvider(SECRET_KEY)
  private val fakeApiKeyJpaRepository = FakeApiKeyJpaRepository()

  private lateinit var apiKeyPair: ApiKeyPair

  @BeforeEach
  fun setup() {
    val signature: ApiKeyPair = paymentTokenProvider.issue()
    apiKeyPair = signature

    val apiKey: ApiKey = ApiKey.create(apiKeyPair.plain, apiKeyPair.lookupKey)

    fakeApiKeyJpaRepository.save(apiKey)
  }

  @Test
  @DisplayName("Api 키 검증 성공")
  fun successVerifyApiKey() {
    // given
    val pair: ApiKeyPair = apiKeyPair

    // when
    val verify: Boolean = paymentTokenProvider.verify(pair.plain, pair.hashed)

    // then
    assertThat(verify).isTrue()
  }

  @Test
  @DisplayName("Api 키 검증 싪패")
  fun failureVerifyApiKey() {
    // given
    val pair = apiKeyPair

    // when
    val verify: Boolean = paymentTokenProvider.verify("asdfasdfasdfasdfasdfasdf", pair.hashed)

    // then
    assertThat(verify).isFalse()
  }

  @Test
  @DisplayName("apiKey 조회 가능")
  fun findApiKey() {
    val pair: ApiKeyPair = apiKeyPair

    val apiKey: ApiKeyEntity? = fakeApiKeyJpaRepository.findByLookUpKey(pair.lookupKey)

    assertThat(apiKey).isNotNull()
    assertThat(apiKey?.apiKey).isEqualTo(pair.plain)
  }
}

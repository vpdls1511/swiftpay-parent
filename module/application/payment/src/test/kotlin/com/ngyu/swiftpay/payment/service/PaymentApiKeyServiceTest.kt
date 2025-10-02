package com.ngyu.swiftpay.payment.service

import com.ngyu.swiftpay.security.provider.PaymentTokenProvider
import com.ngyu.swiftpay.security.util.HmacEncUtil
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PaymentApiKeyServiceTest {

  private val SECRET_KEY: String = "ck7488ZlcflJ+ZwsY/h9vIzPRjS5KsuAlQS9lBVBgks="
  private val paymentTokenProvider: PaymentTokenProvider = PaymentTokenProvider(SECRET_KEY)

  private lateinit var apiKeyPair: HmacEncUtil.ApiKeyPair

  @BeforeEach
  fun setup() {
    val signature: HmacEncUtil.ApiKeyPair = paymentTokenProvider.issue()
    apiKeyPair = signature
  }

  @Test
  @DisplayName("Api 키 검증 성공")
  fun successVerifyApiKey() {
    // given
    val pair = apiKeyPair

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
}

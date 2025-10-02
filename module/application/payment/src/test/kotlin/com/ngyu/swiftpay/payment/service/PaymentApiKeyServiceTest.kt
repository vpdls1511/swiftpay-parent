package com.ngyu.swiftpay.payment.service

import com.ngyu.swiftpay.security.provider.PaymentTokenProvider
import com.ngyu.swiftpay.security.util.HmacEncUtil
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class PaymentApiKeyServiceTest {

  private val SECRET_KEY: String = "ck7488ZlcflJ+ZwsY/h9vIzPRjS5KsuAlQS9lBVBgks="
  private val paymentTokenProvider: PaymentTokenProvider = PaymentTokenProvider(SECRET_KEY)

  private var apiTestToken: String = ""

  @Test
  @Order(1)
  @DisplayName("Api 키 생성이 가능함")
  fun createApiKey() {
    //given
    val signature: HmacEncUtil.ApiKeyPair = paymentTokenProvider.issue()

    //when
    apiTestToken = signature.plain

    //then
    assertThat(apiTestToken).isNotEmpty()
  }

  @Test
  @DisplayName("Api 키 검증 성공")
  fun successVerifyApiKey() {
    // given
    val plain = apiTestToken

    // when
    val verify: Boolean = paymentTokenProvider.verify(plain)

    // then
    assertThat(verify).isTrue()
  }
}

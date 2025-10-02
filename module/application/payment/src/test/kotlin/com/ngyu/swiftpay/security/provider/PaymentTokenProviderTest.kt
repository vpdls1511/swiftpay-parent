package com.ngyu.swiftpay.security.provider

import com.ngyu.swiftpay.core.logger.logger
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import java.security.SecureRandom
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class PaymentTokenProviderTest {

  private val SERVICE_NAME: String = "SWIFTPAY"
  private val SECRET_KEY: String = "iA/AHX8WZqEjcF4ta6d5N9ma+JZJj3H1J1BSsvWqOY4="

  private val log: Logger = logger()

  @Test
  @DisplayName("HMAC 기반 API-KEY 생성")
  fun getTimestampToString() {
    //given
    val timestamp = System.currentTimeMillis()
    val random = SecureRandom().nextInt().toString()
    val data = "$SERVICE_NAME:$timestamp:$random"

    //when
    val hmac = Mac.getInstance("HmacSHA256")
    hmac.init(SecretKeySpec(SECRET_KEY.toByteArray(), "HmacSHA256"))
    val signature = Base64.getUrlEncoder()
      .withoutPadding()
      .encodeToString(hmac.doFinal(data.toByteArray()))

    val apiKey = listOf(SERVICE_NAME, signature).joinToString(separator = "_")
    log.info(apiKey)

    //then
    assert(apiKey.isNotEmpty())
  }
}

package com.ngyu.swiftpay.security.provider

import com.ngyu.swiftpay.core.constant.ServiceConstant
import com.ngyu.swiftpay.security.util.HmacEncUtil
import com.ngyu.swiftpay.security.util.HmacEncUtil.ApiKeyPair
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class PaymentTokenProvider(
  @Value("\${hmac.secret}") private val HMAC_KEY: String,
) {

  fun issue(): ApiKeyPair {
    val signature = HmacEncUtil.generate(HMAC_KEY)
    val apiKey = "${ServiceConstant.SERVICE_NAME}.${signature}"

    return ApiKeyPair(
      plain = apiKey,
      hashed = HmacEncUtil.hash(apiKey)
    )
  }

}

package com.ngyu.swiftpay.security.provider

import com.ngyu.swiftpay.security.util.HmacEncUtil
import com.ngyu.swiftpay.security.vo.ApiKeyPair
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class PaymentTokenProvider(
  @Value("\${hmac.secret}") private val HMAC_KEY: String,
) {
  fun issue(): ApiKeyPair = HmacEncUtil.generate(HMAC_KEY)
  fun verify(apiKey: String, hashKey: String) : Boolean = HmacEncUtil.verify(apiKey, hashKey)
}

package com.ngyu.swiftpay.security.provider

import com.ngyu.swiftpay.core.domain.constant.ServiceConstant
import com.ngyu.swiftpay.security.util.HmacEncUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class PaymentTokenProvider(
  @Value("\${hmac.secret}")
  private val HMAC_KEY: String,
) {

  fun issue(): String {
    val hmac = HmacEncUtil.generate(HMAC_KEY)

    return listOf(ServiceConstant.SERVICE_NAME, hmac).joinToString("-")
  }

}

package com.ngyu.swiftpay.security.util

import java.security.SecureRandom
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HmacEncUtil {
  fun generate(secretKey: String): String {
    val timestamp = System.currentTimeMillis()
    val random = SecureRandom().nextInt().toString()
    val data = "$timestamp:$random"

    val hmac = Mac.getInstance("HmacSHA256")
    hmac.init(SecretKeySpec(secretKey.toByteArray(), "HmacSHA256"))
    val signature = Base64.getUrlEncoder()
      .withoutPadding()
      .encodeToString(hmac.doFinal(data.toByteArray()))

    return signature
  }
}

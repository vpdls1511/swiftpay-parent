package com.ngyu.swiftpay.security.util

import org.springframework.security.crypto.bcrypt.BCrypt
import java.security.SecureRandom
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HmacEncUtil {

  data class ApiKeyPair (val plain: String, val hashed: String )

  fun generate(secretKey: String): ApiKeyPair {
    val timestamp = System.currentTimeMillis()
    val random = SecureRandom().nextInt().toString()
    val data = "$timestamp:$random"

    val signature = hmac(data, secretKey)
    val hashKey = hash(signature)

    return ApiKeyPair(signature, hashKey)
  }

  fun hash(apiKey: String): String = BCrypt.hashpw(apiKey, BCrypt.gensalt())
  fun verify(apiKey: String, hashKey: String): Boolean = BCrypt.checkpw(apiKey, hashKey)

  private fun hmac(data: String, secretKey: String): String {
    val hmac = Mac.getInstance("HmacSHA256")
    hmac.init(SecretKeySpec(secretKey.toByteArray(), "HmacSHA256"))

    return Base64.getUrlEncoder()
      .withoutPadding()
      .encodeToString(hmac.doFinal(data.toByteArray()))
  }
}

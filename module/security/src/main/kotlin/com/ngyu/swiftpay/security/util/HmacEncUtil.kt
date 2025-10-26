package com.ngyu.swiftpay.security.util

import com.ngyu.swiftpay.core.common.constant.ServiceConstant
import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.security.vo.ApiKeyPair
import org.springframework.security.crypto.bcrypt.BCrypt
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HmacEncUtil {

  private val log = logger()

  /**
   * 시크릿 키를 기반으로 HMAC과 hash값 전달
   */
  fun generate(secretKey: String): ApiKeyPair {
    val timestamp = System.currentTimeMillis()
    val random = SecureRandom().nextInt().toString()
    val data = "$timestamp:$random"

    val signature = hmac(data, secretKey)
    val apiKey = "${ServiceConstant.SERVICE_NAME}.${signature}"

    val hashKey = hash(apiKey)
    val lookupKey = sha256(apiKey)

    return ApiKeyPair(apiKey, lookupKey, hashKey)
  }

  /**
   * api key 값을 DB에 저장하기 위해 hash
   */
  fun hash(apiKey: String): String {
    val sha256hash = sha256(apiKey)

    log.trace("plain key: $apiKey")
    log.trace("hash key: $sha256hash")

    return BCrypt.hashpw(sha256hash, BCrypt.gensalt())
  }


  /**
   * DB에 저장된 hash 값과 api request key 검증
   */
  fun verify(apiKey: String, hashKey: String): Boolean {
    val sha256hash = sha256(apiKey)

    return BCrypt.checkpw(sha256hash, hashKey)
  }

  /**
   * data 를 hmac 으로 변환
   */
  private fun hmac(data: String, secretKey: String): String {
    val hmac = Mac.getInstance("HmacSHA256")
    hmac.init(SecretKeySpec(secretKey.toByteArray(), "HmacSHA256"))

    return Base64.getUrlEncoder()
      .withoutPadding()
      .encodeToString(hmac.doFinal(data.toByteArray()))
  }

  /**
   * SHA-256으로 변환을 위한 메서드
   *
   *
   * BCrypt 는 72바이트까지만 가능하기 때문에.. HMAC 값을 SHA-256으로 한번 더 해싱
   */
  private fun sha256(data: String): String {
    return MessageDigest.getInstance("SHA-256")
      .digest(data.toByteArray())
      .joinToString("") { String.format("%02x", it) }
  }
}

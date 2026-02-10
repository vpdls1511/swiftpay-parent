package me.ngyu.swiftpay.common.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
  private val jwtProperties: JwtProperties
) {
  private val secretKey: SecretKey by lazy {
    Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
  }

  fun createAccessToken(uuid: String, userId: Long, role: String): String {
    return this.createToken(uuid, userId, role, jwtProperties.accessExpiration);
  }

  fun createRefreshToken(uuid: String): String {
    return this.createToken(uuid, null, null, jwtProperties.refreshExpiration);
  }

  fun parseToken(token: String): BaseTokenPayload {
    val claims = Jwts.parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(token)
      .payload

    return BaseTokenPayload(
      userId = claims["userId", Integer::class.java]?.toLong(),
      role = claims["role", String::class.java]
    )
  }

  private fun createToken(uuid: String, userId: Long?, role: String?, expired: Long): String {
    val now = Date()

    return Jwts.builder()
      .subject(uuid)
      .claim("userId", userId)
      .claim("role", role)
      .expiration(Date(now.time + expired))
      .signWith(secretKey)
      .compact()
  }

}

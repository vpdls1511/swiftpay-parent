package me.ngyu.swiftpay.common.security.jwt

data class BaseTokenPayload(
  val userId: Long?,
  val role: String?
) {
}

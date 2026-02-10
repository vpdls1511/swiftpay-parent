package me.ngyu.swiftpay.auth.api.dto

data class TokenDto (
  val accessToken: String,
  val refreshToken: String,
)

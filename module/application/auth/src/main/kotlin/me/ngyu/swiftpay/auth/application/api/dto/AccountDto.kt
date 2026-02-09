package me.ngyu.swiftpay.auth.application.api.dto

data class AccountRequest(
  val username: String,
  val password: String,

  val name: String,
  val email: String,
  val phone: String
) {}

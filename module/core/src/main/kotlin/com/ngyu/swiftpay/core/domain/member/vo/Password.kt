package com.ngyu.swiftpay.core.domain.member.vo

@JvmInline
value class Password private constructor(val value: String) {
  companion object {
    fun of(password: String): Password {

      require(password.isNotBlank()) { "비밀번호는 필수입니다." }
      require(password.length >= 8) { "비밀번호는 최소 8자 이상이어야 합니다." }
      require(password.any { it.isDigit() }) { "비밀번호는 최소 1개의 숫자를 포함해야 합니다." }
      require(password.any { it.isUpperCase() }) { "비밀번호는 최소 1개의 대문자를 포함해야 합니다." }
      require(password.any { it.isLowerCase() }) { "비밀번호는 최소 1개의 소문자를 포함해야 합니다." }
      require(password.any { it in "@#$%^&+=!" }) { "비밀번호는 최소 1개의 특수문자를 포함해야 합니다." }

      return Password(password)
    }

    fun ofEncrypt(encPassword: String): Password {
      return Password(encPassword)
    }
  }

  override fun toString(): String {
    return "Password(*********)"
  }
}

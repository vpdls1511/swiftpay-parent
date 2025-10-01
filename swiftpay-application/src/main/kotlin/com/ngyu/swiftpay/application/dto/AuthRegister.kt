package com.ngyu.swiftpay.application.dto

import com.ngyu.swiftpay.core.domain.Member
import com.ngyu.swiftpay.core.domain.vo.Password
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AuthRegisterRequest(
  @field:NotBlank(message = "사용자명은 필수입니다.")
  @field:Size(min = 4, max = 20, message = "사용자명은 4-20자 사이여야 합니다.")
  val username: String,

  @field:NotBlank(message = "비밀번호는 필수입니다.")
  val password: String,  // Password VO에서 상세 검증

  @field:NotBlank(message = "이름은 필수입니다.")
  val name: String,

  @field:NotBlank(message = "이메일은 필수입니다.")
  @field:Email(message = "올바른 이메일 형식이 아닙니다.")
  val email: String
) {

  fun toDomain(): Member {
    return Member.createMember(
      username = username,
      password = Password.of(password),
      name = name,
      email = email
    )
  }

}

data class AuthRegisterResponse(val apiToken: String)

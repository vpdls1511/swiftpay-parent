package com.ngyu.swiftpay.core.domain

import com.ngyu.swiftpay.core.domain.vo.Password
import java.time.LocalDateTime

data class Member(
  val id: Long? = null,
  val username: String,
  val password: Password,
  val name: String,
  val email: String,
  val createdAt: LocalDateTime,
  val status: MemberStatus
) {
  companion object {
    fun createMember(username: String, password: Password, name: String, email: String): Member {
      return Member(
        id = null,
        username = username,
        password = password,
        name = name,
        email = email,
        createdAt = LocalDateTime.now(),
        status = MemberStatus.PENDING
      )
    }
  }
}

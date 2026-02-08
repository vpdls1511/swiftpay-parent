package me.ngyu.swiftpay.auth.domain.account

import jakarta.persistence.*
import me.ngyu.swiftpay.common.domain.BaseEntity

@Entity
@Table(name = "account")
class Account(
  @Column(nullable = false, unique = true, length = 36)
  val uuid: String,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  var role: AccountRole,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  var status: AccountStatus = AccountStatus.ACTIVE
) : BaseEntity() {

  // 도메인 로직
  fun changeRole(newRole: AccountRole) {
    this.role = newRole
  }

  fun activate() {
    this.status = AccountStatus.ACTIVE
  }

  fun suspend() {
    this.status = AccountStatus.SUSPENDED
  }

  fun delete() {
    this.status = AccountStatus.DELETED
  }
}

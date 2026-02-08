package me.ngyu.swiftpay.auth.domain.account

import jakarta.persistence.*
import me.ngyu.swiftpay.common.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "account_credentials")
class AccountCredentials(
  @Id
  val accountId: Long,

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "account_id")
  val account: Account,

  var username: String,
  var password: String,

  var passwordChangedAt: LocalDateTime
) : BaseTimeEntity()

package me.ngyu.swiftpay.auth.domain.account

import jakarta.persistence.*
import me.ngyu.swiftpay.common.domain.BaseTimeEntity

@Entity
@Table(name = "account_profile")
class AccountProfile(
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "account_id")
  val account: Account,

  var name: String,
  var email: String,
  var phone: String,
) : BaseTimeEntity() {

  @Id
  var accountId: Long = 0
    private set
}

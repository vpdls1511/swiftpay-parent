package me.ngyu.swiftpay.auth.domain.account

import jakarta.persistence.*
import me.ngyu.swiftpay.common.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "account_credentials")
class AccountCredentials(
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "account_id")
  val account: Account,

  @Column(nullable = false, unique = true, length = 50)
  var username: String,

  @Column(nullable = false, length = 255)
  var password: String,

  @Column(nullable = false)
  var passwordChangedAt: LocalDateTime = LocalDateTime.now()
) : BaseTimeEntity() {

  @Id
  var accountId: Long = 0  // @MapsId가 자동으로 account.id를 할당
    private set
}

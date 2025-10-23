package com.ngyu.swiftpay.infrastructure.db.persistent.bank

import com.ngyu.swiftpay.core.domain.bank.BankAccountStatus
import com.ngyu.swiftpay.core.domain.bank.BankCode
import com.ngyu.swiftpay.core.domain.money.Currency
import com.ngyu.swiftpay.infrastructure.db.persistent.common.BaseTimeEntity
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "bank_account")
class BankAccountEntity(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  @Convert(converter = BankCodeConverter::class)
  @Column(nullable = false, length = 20)
  var bankCode: BankCode,

  @Column(nullable = false, unique = true, length = 20)
  var accountNumber: String,

  @Column(nullable = false, length = 100)
  var accountHolder: String,

  @Column(nullable = false, precision = 19, scale = 2)
  var amount: BigDecimal,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 3)
  var currency: Currency,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  var status: BankAccountStatus
): BaseTimeEntity()

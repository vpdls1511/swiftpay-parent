package com.ngyu.swiftpay.infrastructure.db.persistent.payment

import com.ngyu.swiftpay.core.domain.money.Currency
import com.ngyu.swiftpay.core.domain.payment.model.PayMethod
import com.ngyu.swiftpay.core.domain.payment.model.PayStatus
import com.ngyu.swiftpay.infrastructure.db.persistent.common.BaseTimeEntity
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "payments")
class PaymentEntity(
  @Id
  @Column(length = 50)
  val id: String,

  @Column(nullable = false, length = 100)
  val merchantId: String,

  @Column(nullable = false, length = 100)
  val orderId: String,

  @Column(nullable = false, length = 200)
  val orderName: String,

  @Column(nullable = false, precision = 19, scale = 2)
  val amount: BigDecimal,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 3)
  val currency: Currency = Currency.KRW,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  val method: PayMethod,

  @Embedded
  val methodDetail: PayMethodDetailsEmbeddable,

  @Column(length = 500)
  val successUrl: String? = null,

  @Column(length = 500)
  val cancelUrl: String? = null,

  @Column(length = 500)
  val failureUrl: String? = null,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  val status: PayStatus,

  @Column
  val reason: String? = null,

  @Column(unique = true, length = 100)
  val idempotencyKey: String? = null,

  @Column(nullable = true)
  val settlementId: String? = null,
) : BaseTimeEntity()

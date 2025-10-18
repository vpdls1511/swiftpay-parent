package com.ngyu.swiftpay.infrastructure.db.persistent.payment

import com.ngyu.swiftpay.core.domain.payment.model.PaymentCardType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class PayMethodDetailsEmbeddable(
  @Column(length = 50)
  val type: String,

  // 카드 정보
  @Column(length = 20)
  val cardNumber: String? = null,

  @Column(length = 4)
  val cardExpiry: String? = null,  // YYMM

  @Column(length = 4)
  val cardCvc: String? = null,

  @Column(length = 5)
  val cardType: PaymentCardType? = null,

  @Column
  val installmentPlan: Int? = null,

  @Column
  val useCardPoint: Boolean? = null,

  // 계좌이체 정보
  @Column(length = 50)
  val bankCode: String? = null,

  @Column(length = 50)
  val accountNumber: String? = null
)

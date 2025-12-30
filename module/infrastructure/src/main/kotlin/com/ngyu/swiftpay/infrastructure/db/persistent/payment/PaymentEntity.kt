package com.ngyu.swiftpay.infrastructure.db.persistent.payment

import com.ngyu.swiftpay.core.domain.payment.PaymentMethod
import com.ngyu.swiftpay.core.domain.payment.PaymentStatus
import com.ngyu.swiftpay.core.vo.Currency
import com.ngyu.swiftpay.infrastructure.db.persistent.common.BaseTimeEntity
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "payments")
class PaymentEntity(
  @Id
  val id: Long,

  @Column(length = 50)
  val paymentId: String,

  @Column(nullable = false, length = 100)
  val merchantId: Long,

  @Column(nullable = false, length = 100)
  val orderId: Long,

  @Column(nullable = false, length = 200)
  val orderName: String,

  @Column(nullable = false, precision = 19, scale = 2)
  val amount: BigDecimal,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 3)
  val currency: Currency = Currency.KRW,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  val method: PaymentMethod,

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
  val status: PaymentStatus,

  @Column
  val reason: String? = null,

  @Column
  val acquirerTransactionId: String? = null, // 거래번호

  @Column
  val acquirerApprovalNumber: String? = null, // 승인번호 or 이체번호

  @Column
  val acquirerResponseCode: String? = null, // 승인 코드

  @Column
  val acquirerMessage: String? = null, // 승인 메시지


  @Column(unique = true, length = 100)
  val idempotencyKey: String? = null,
) : BaseTimeEntity()

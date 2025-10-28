package com.ngyu.swiftpay.core.domain.payment

import com.ngyu.swiftpay.core.common.exception.InvalidPaymentStatusException
import com.ngyu.swiftpay.core.domain.BaseDomain
import com.ngyu.swiftpay.core.domain.payment.vo.PaymentMethodDetails
import com.ngyu.swiftpay.core.vo.Currency
import com.ngyu.swiftpay.core.vo.Money
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * ### 결제 도메인 엔티티
 *
 * 결제의 생명주기를 관리하며, 결제 요청부터 완료/취소까지의
 * 모든 상태와 정보를 포함한다.
 */
class Payment(
  override val id: Long,

  // 기본정보
  val paymentId: String,              // 고유 ID
  val merchantId: String,             // 가맹점 Id
  val orderId: String,                // 가맹점의 주문번호
  val orderName: String,              // 상품 이름
  val amount: Money,                  // 결제 금액

  // 결제 수단 정보
  val method: PaymentMethod,              // 결제 수단
  val methodDetail: PaymentMethodDetails, // 결제 상세정보 (예: 카드)

  // 콜백 URL
  val successUrl: String? = null,
  val cancelUrl: String? = null,
  val failureUrl: String? = null,

  // 상태관리
  val status: PaymentStatus,
  val reason: String? = null,
  val idempotencyKey: String? = null,   // 중복 결제 방지 키
  val settlementId: String? = null,     // 정산 관리 Id

  // 시스템 정보
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
) : BaseDomain<Long>() {

  companion object {
    val PREFIX = "SWIFT_PAYMENT_"
    val PADDING_LENGTH = 20

    fun createPaymentId(seq: Long): String = "${PREFIX}${seq.toString().padStart(PADDING_LENGTH, '0')}"

    /**
     * 새로운 결제를 생성한다.
     * 초기 상태는 PENDING이며, 고유한 결제 ID가 자동 생성된다.
     */
    fun create(
      paymentSeq: Long,
      paymentId: String,
      merchantId: String,
      orderId: String,
      orderName: String,
      amount: BigDecimal,
      currency: Currency,
      method: PaymentMethod,
      methodDetail: PaymentMethodDetails,
      successUrl: String? = null,
      cancelUrl: String? = null,
      failureUrl: String? = null,
      idempotencyKey: String? = null,
    ): Payment {
      val now = LocalDateTime.now()
      return Payment(
        id = paymentSeq,
        paymentId = paymentId,
        merchantId = merchantId,
        orderId = orderId,
        orderName = orderName,
        amount = Money(amount, currency),
        method = method,
        methodDetail = methodDetail,
        successUrl = successUrl,
        cancelUrl = cancelUrl,
        failureUrl = failureUrl,
        status = PaymentStatus.PENDING,
        idempotencyKey = idempotencyKey,
        createdAt = now,
        updatedAt = now
      )
    }
  }

  // ===== 상태 전이 메서드 =====

  // Payment 도메인
  fun inProgress(): Payment {
    if (status != PaymentStatus.PENDING) {
      throw InvalidPaymentStatusException("결제 대기 상태가 아닙니다. 현재: $status")
    }
    return copy(status = PaymentStatus.IN_PROGRESS)
  }

  fun success(): Payment {
    if (status != PaymentStatus.IN_PROGRESS) {
      throw InvalidPaymentStatusException("결제 중 상태가 아닙니다. 현재: $status")
    }
    return copy(status = PaymentStatus.SUCCEEDED)
  }

  fun cancel(): Payment {
    if (status != PaymentStatus.IN_PROGRESS) {
      throw InvalidPaymentStatusException("결제 중 상태가 아닙니다. 현재: $status")
    }
    return copy(status = PaymentStatus.CANCELLED)
  }

  fun failed(reason: String): Payment {
    if (status != PaymentStatus.IN_PROGRESS) {
      throw InvalidPaymentStatusException("결제 중 상태가 아닙니다. 현재: $status")
    }
    return copy(status = PaymentStatus.FAILED, reason = reason)
  }
  // ===== 불변 복제(copy) 메서드 =====
  private fun copy(
    id: Long = this.id,
    paymentId: String = this.paymentId,
    merchantId: String = this.merchantId,
    orderId: String = this.orderId,
    orderName: String = this.orderName,
    amount: Money = this.amount,
    method: PaymentMethod = this.method,
    methodDetail: PaymentMethodDetails = this.methodDetail,
    successUrl: String? = this.successUrl,
    cancelUrl: String? = this.cancelUrl,
    failureUrl: String? = this.failureUrl,
    status: PaymentStatus = this.status,
    reason: String? = this.reason,
    idempotencyKey: String? = this.idempotencyKey,
    settlementId: String? = this.settlementId,
    createdAt: LocalDateTime = this.createdAt,
    updatedAt: LocalDateTime = LocalDateTime.now()
  ): Payment {
    return Payment(
      id = id,
      paymentId = paymentId,
      merchantId = merchantId,
      orderId = orderId,
      orderName = orderName,
      amount = amount,
      method = method,
      methodDetail = methodDetail,
      successUrl = successUrl,
      cancelUrl = cancelUrl,
      failureUrl = failureUrl,
      status = status,
      reason = reason,
      idempotencyKey = idempotencyKey,
      settlementId = settlementId,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }
}

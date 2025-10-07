package com.ngyu.swiftpay.core.domain.payment

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * 결제의 생명주기를 관리하는 도메인
 */
data class Payment(
  // 기본정보
  val id: String,                     // 고유 ID
  val apiPairKey: String,             // 주문시 사용한 pairKey
  val orderId: String,                // 가맹점의 주문번호
  val orderName: String,              // 상품 이름
  val amount: BigDecimal,             // 상품 가격
  val currency: String = "KRW",       // 통화

  // 결제 수단 정보
  val method: PayStatus,              // 결제 수단
  val methodDetail: PayMethodDetails, // 결제 상세정보 ( CARD일 경우, 옵션 )

  // 옵션 - 콜백 URL
  val successUrl: String? = null,
  val cancelUrl: String? = null,
  val failureUrl: String? = null,

  // 상태관리
  val status: PayStatus,
  val idempotencyKey: String?,        // 중복 결제 방지 키

  // 시스템정보
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
) {
  companion object {
    fun create(
      apiPairKey: String,
      orderId: String,
      orderName: String,
      amount: BigDecimal,
      currency: String = "KRW",
      method: PayStatus,
      methodDetail: PayMethodDetails,
      successUrl: String? = null,
      cancelUrl: String? = null,
      failureUrl: String? = null,
      idempotencyKey: String? = null,
    ): Payment {
      val now = LocalDateTime.now()
      return Payment(
        id = generatePaymentId(),
        apiPairKey = apiPairKey,
        orderId = orderId,
        orderName = orderName,
        amount = amount,
        currency = currency,
        method = method,
        methodDetail = methodDetail,
        successUrl = successUrl,
        cancelUrl = cancelUrl,
        failureUrl = failureUrl,
        status = PayStatus.PENDING,
        idempotencyKey = idempotencyKey,
        createdAt = now,
        updatedAt = now
      )
    }

    private fun generatePaymentId(): String {
      return "swift_pay_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }
  }
}

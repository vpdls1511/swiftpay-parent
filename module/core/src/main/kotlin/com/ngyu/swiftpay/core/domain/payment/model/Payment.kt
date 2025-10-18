package com.ngyu.swiftpay.core.domain.payment.model

import com.ngyu.swiftpay.core.domain.money.Currency
import com.ngyu.swiftpay.core.domain.money.Money
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * ### 결제 도메인 엔티티
 *
 * 결제의 생명주기를 관리하며, 결제 요청부터 완료/취소까지의
 *
 * 모든 상태와 정보를 포함한다.
 */
data class Payment(
  // 기본정보
  val id: String,                     // 고유 ID
  val merchantId: String,             // 가맹점 Id
  val orderId: String,                // 가맹점의 주문번호
  val orderName: String,              // 상품 이름
  val amount: Money,             // 상품 가격

  // 결제 수단 정보
  val method: PayMethod,              // 결제 수단
  val methodDetail: PayMethodDetails, // 결제 상세정보 ( CARD일 경우, 옵션 )

  // 옵션 - 콜백 URL
  val successUrl: String? = null,
  val cancelUrl: String? = null,
  val failureUrl: String? = null,

  // 상태관리
  val status: PayStatus,
  val reason: String? = null,
  val idempotencyKey: String?,        // 중복 결제 방지 키

  val settlementId: String? = null, // 정산 관리 Id

  // 시스템정보
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
) {
  companion object {
    /**
     * 새로운 결제를 생성한다.
     *
     * 초기 상태는 PENDING이며, 고유한 결제 ID가 자동 생성된다.
     *
     * @param merchantId 가맹점 아이디
     * @param orderId 가맹점 주문 번호
     * @param orderName 주문 상품명
     * @param amount 결제 금액
     * @param currency 통화 (기본: KRW)
     * @param method 결제 수단
     * @param methodDetail 결제 수단 상세
     * @param successUrl 성공 콜백 URL
     * @param cancelUrl 취소 콜백 URL
     * @param failureUrl 실패 콜백 URL
     * @param idempotencyKey 중복 방지 키
     * @return 생성된 Payment 도메인 객체
     */
    fun create(
      merchantId: String,
      orderId: String,
      orderName: String,
      amount: BigDecimal,
      currency: Currency,
      method: PayMethod,
      methodDetail: PayMethodDetails,
      successUrl: String? = null,
      cancelUrl: String? = null,
      failureUrl: String? = null,
      idempotencyKey: String? = null,
    ): Payment {
      val now = LocalDateTime.now()
      return Payment(
        id = generatePaymentId(),
        merchantId = merchantId,
        orderId = orderId,
        orderName = orderName,
        amount = Money(amount, currency),
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

 /**
     * 고유한 결제 ID를 생성한다.
     *
     * 형식: swift_pay_{timestamp}_{random}
     *
     * 예시: swift_pay_1696752000000_5837
     */
    private fun generatePaymentId(): String {
      return "swift_pay_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }
  }

  /**
   * 상태관리를 위한 메서드 리스트
   * - inProgress
   * - succeed
   * - cancelled
   * - failed
   */
  fun inProgress(): Payment {
    require(status == PayStatus.PENDING) { "결제 대기 상태가 아닙니다." }
    return this.copy(
      status = PayStatus.IN_PROGRESS,
      updatedAt = LocalDateTime.now(),
    )
  }
  fun success(): Payment {
    require(status == PayStatus.PENDING) { "결제 대기 상태가 아닙니다." }
    return this.copy(
      status = PayStatus.SUCCEEDED,
      updatedAt = LocalDateTime.now(),
    )
  }
  fun cancel(): Payment {
    require(status == PayStatus.PENDING) { "결제 대기 상태가 아닙니다." }
    return this.copy(
      status = PayStatus.CANCELLED,
      updatedAt = LocalDateTime.now(),
    )
  }
  fun failed(reason: String): Payment {
    require(status == PayStatus.PENDING) { "결제 대기 상태가 아닙니다." }
    return this.copy(
      status = PayStatus.FAILED,
      reason = reason,
      updatedAt = LocalDateTime.now(),
    )
  }
}

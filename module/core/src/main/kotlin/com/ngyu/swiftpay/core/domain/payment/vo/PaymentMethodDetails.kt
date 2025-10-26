package com.ngyu.swiftpay.core.domain.payment.vo

import com.ngyu.swiftpay.core.domain.payment.PaymentCardType

// 결제 수단별 상세 정보
sealed class PaymentMethodDetails {

  data class Card(
    val cardNumber: String?,          // 카드번호 (토큰화...)
    val cardExpiry: String?,          // 유효기간 (YYMM)
    val cardCvc: String?,             // CVC
    val installmentPlan: Int? = 0,    // 할부 개월 (0: 일시불)
    val cardType: PaymentCardType?,
    val useCardPoint: Boolean = false // 카드 포인트 사용 여부
  ) : PaymentMethodDetails()

  data class BankTransfer(
    val bankCode: String?,        // 은행 코드
    val accountNumber: String?    // 계좌번호
  ) : PaymentMethodDetails()

}

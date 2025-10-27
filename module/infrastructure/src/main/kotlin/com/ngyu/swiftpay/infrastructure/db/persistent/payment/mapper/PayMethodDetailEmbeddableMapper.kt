package com.ngyu.swiftpay.infrastructure.db.persistent.payment.mapper

import com.ngyu.swiftpay.core.domain.payment.vo.PaymentMethodDetails
import com.ngyu.swiftpay.infrastructure.db.persistent.payment.PayMethodDetailsEmbeddable

object PayMethodDetailsEmbeddableMapper {

  /**
   * 도메인 모델을 Embeddable로 변환
   */
  fun toEmbeddable(domain: PaymentMethodDetails): PayMethodDetailsEmbeddable {
    return when (domain) {
      is PaymentMethodDetails.Card -> PayMethodDetailsEmbeddable(
        type = "CARD",
        cardNumber = domain.cardNumber,
        cardExpiry = domain.cardExpiry,
        cardCvc = domain.cardCvc,
        cardType = domain.cardType,
        installmentPlan = domain.installmentPlan,
        useCardPoint = domain.useCardPoint
      )

      is PaymentMethodDetails.BankTransfer -> PayMethodDetailsEmbeddable(
        type = "BANK_TRANSFER",
        bankCode = domain.bankCode,
        accountNumber = domain.accountNumber
      )
    }
  }

  /**
   * Embeddable을 도메인 모델로 변환
   */
  fun toDomain(embeddable: PayMethodDetailsEmbeddable): PaymentMethodDetails {
    return when (embeddable.type) {
      "CARD" -> PaymentMethodDetails.Card(
        cardNumber = requireNotNull(embeddable.cardNumber) { "카드번호 필수" },
        cardExpiry = requireNotNull(embeddable.cardExpiry) { "유효기간 필수" },
        cardCvc = requireNotNull(embeddable.cardCvc) { "CVC 필수" },
        cardType = requireNotNull(embeddable.cardType) { "카드타입 필수" },
        installmentPlan = embeddable.installmentPlan ?: 0,
        useCardPoint = embeddable.useCardPoint ?: false
      )

      "BANK_TRANSFER" -> PaymentMethodDetails.BankTransfer(
        bankCode = requireNotNull(embeddable.bankCode) { "은행코드 필수" },
        accountNumber = requireNotNull(embeddable.accountNumber) { "계좌번호 필수" }
      )

      else -> throw IllegalArgumentException("알 수 없는 타입: ${embeddable.type}")
    }
  }
}

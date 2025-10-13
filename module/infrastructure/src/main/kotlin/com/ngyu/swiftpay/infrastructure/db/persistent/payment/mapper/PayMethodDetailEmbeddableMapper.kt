package com.ngyu.swiftpay.infrastructure.db.persistent.payment.mapper

import com.ngyu.swiftpay.core.domain.payment.PayMethodDetails
import com.ngyu.swiftpay.infrastructure.db.persistent.payment.PayMethodDetailsEmbeddable

object PayMethodDetailsEmbeddableMapper {

  /**
   * 도메인 모델을 Embeddable로 변환
   */
  fun toEmbeddable(domain: PayMethodDetails): PayMethodDetailsEmbeddable {
    return when (domain) {
      is PayMethodDetails.Card -> PayMethodDetailsEmbeddable(
        type = "CARD",
        cardNumber = domain.cardNumber,
        cardExpiry = domain.cardExpiry,
        cardCvc = domain.cardCvc,
        installmentPlan = domain.installmentPlan,
        useCardPoint = domain.useCardPoint
      )

      is PayMethodDetails.BankTransfer -> PayMethodDetailsEmbeddable(
        type = "BANK_TRANSFER",
        bankCode = domain.bankCode,
        accountNumber = domain.accountNumber
      )
    }
  }

  /**
   * Embeddable을 도메인 모델로 변환
   */
  fun toDomain(embeddable: PayMethodDetailsEmbeddable): PayMethodDetails {
    return when (embeddable.type) {
      "CARD" -> PayMethodDetails.Card(
        cardNumber = embeddable.cardNumber,
        cardExpiry = embeddable.cardExpiry,
        cardCvc = embeddable.cardCvc,
        installmentPlan = embeddable.installmentPlan,
        useCardPoint = embeddable.useCardPoint ?: false
      )

      "BANK_TRANSFER" -> PayMethodDetails.BankTransfer(
        bankCode = embeddable.bankCode,
        accountNumber = embeddable.accountNumber
      )

      else -> throw IllegalArgumentException("알 수 없는 결제 수단 타입입니다: ${embeddable.type}")
    }
  }
}

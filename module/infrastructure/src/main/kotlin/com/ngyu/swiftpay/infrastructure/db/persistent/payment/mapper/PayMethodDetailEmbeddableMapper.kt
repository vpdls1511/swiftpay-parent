package com.ngyu.swiftpay.infrastructure.db.persistent.payment.mapper

import com.ngyu.swiftpay.core.domain.payment.model.PaymentMethodDetails
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
        cardNumber = embeddable.cardNumber,
        cardExpiry = embeddable.cardExpiry,
        cardCvc = embeddable.cardCvc,
        cardType = embeddable.cardType,
        installmentPlan = embeddable.installmentPlan,
        useCardPoint = embeddable.useCardPoint ?: false
      )

      "BANK_TRANSFER" -> PaymentMethodDetails.BankTransfer(
        bankCode = embeddable.bankCode,
        accountNumber = embeddable.accountNumber
      )

      else -> throw IllegalArgumentException("알 수 없는 결제 수단 타입입니다: ${embeddable.type}")
    }
  }
}

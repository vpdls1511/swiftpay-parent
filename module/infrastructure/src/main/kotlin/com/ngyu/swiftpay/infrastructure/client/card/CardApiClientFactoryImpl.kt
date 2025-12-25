package com.ngyu.swiftpay.infrastructure.client.card

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.vo.PaymentMethodDetails
import com.ngyu.swiftpay.core.port.client.CardApiClientFactory
import com.ngyu.swiftpay.core.port.client.CardApiClientPort
import com.ngyu.swiftpay.infrastructure.client.card.mock.MockExternalCardApiClient
import org.springframework.stereotype.Component

@Component
class CardApiClientFactoryImpl(
  private val mockCardApiClient: MockExternalCardApiClient
): CardApiClientFactory {
  override fun getClient(domain: Payment): CardApiClientPort {
    val cardDetail = domain.methodDetail as PaymentMethodDetails.Card
    val cardBin = cardDetail.cardNumber.substring(0,2)

    // TODO - 우선, 모두 mock으로. 추후 검증 안된 카드 번호는 exception 처리 해야함
    return when(cardBin) {
      "01" -> mockCardApiClient
      "02" -> mockCardApiClient
      else -> mockCardApiClient
    }
  }
}

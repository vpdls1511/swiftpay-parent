package com.ngyu.swiftpay.infrastructure.client.card.mock

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.port.client.CardApiClientPort
import com.ngyu.swiftpay.core.port.client.dto.CardApprovalResult
import com.ngyu.swiftpay.core.vo.Money
import org.springframework.stereotype.Component
import java.util.*

@Component
class MockExternalCardApiClient: CardApiClientPort {
  override fun shouldCanPayment(domain: Payment): Boolean {
    return true
  }

  override fun approve(domain: Payment): CardApprovalResult {
    // TODO - 추후 통화가 달라지면 domain.amount 의 currency에따라 새로 생성해줘야함.
    val isSuccess = domain.amount <= Money.won(10000)

    return CardApprovalResult(
      isSuccess = isSuccess,
      approvalNumber = if (isSuccess) UUID.randomUUID().toString() else null,
      transferNumber = if (isSuccess) UUID.randomUUID().toString() else null,
      message = if (isSuccess) "승인" else "한도초과"
    )
  }
}

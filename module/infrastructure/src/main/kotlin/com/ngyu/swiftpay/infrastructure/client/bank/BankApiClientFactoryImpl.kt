package com.ngyu.swiftpay.infrastructure.client.bank

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.vo.PaymentMethodDetails
import com.ngyu.swiftpay.core.port.client.BankApiClientFactory
import com.ngyu.swiftpay.core.port.client.BankApiClientPort
import com.ngyu.swiftpay.infrastructure.client.bank.mock.MockExternalBankApiClient
import org.springframework.stereotype.Component

@Component
class BankApiClientFactoryImpl(
  private val mockBankApiClient: MockExternalBankApiClient
): BankApiClientFactory {
  override fun getClient(domain: Payment): BankApiClientPort {
    val bankDetail = domain.methodDetail as PaymentMethodDetails.BankTransfer

    // TODO - 우선, 모두 mock으로. 추후 검증 안된 은행 코드는 exception 처리 해야함
    return when(bankDetail.bankCode) {
      "01" -> mockBankApiClient
      "02" -> mockBankApiClient
      else -> mockBankApiClient
    }
  }
}

package com.ngyu.swiftpay.infrastructure.client.bank.mock

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.port.client.BankApiClientPort
import com.ngyu.swiftpay.core.port.client.dto.BankTransferResult
import org.springframework.stereotype.Component
import java.util.*
import kotlin.random.Random

@Component
class MockExternalBankApiClient: BankApiClientPort {
  override fun shouldCanPayment(domain: Payment): Boolean {
    return true
  }

  override fun transfer(domain: Payment): BankTransferResult {
    val isSuccess = Random.nextBoolean()

    return BankTransferResult(
      isSuccess = isSuccess,
      transactionId = if (isSuccess) UUID.randomUUID().toString() else null,
      approvalNumber = if (isSuccess) UUID.randomUUID().toString() else null,
      message = if (isSuccess) "이체완료" else "잔액부족"
    )
  }

}

package com.ngyu.swiftpay.payment.application.service.bank

import com.ngyu.swiftpay.core.port.repository.BankAccountRepository
import com.ngyu.swiftpay.core.domain.bank.BankCode
import com.ngyu.swiftpay.core.port.generator.AccountNumberGenerator
import org.springframework.stereotype.Service

@Service
class AccountNumberService(
  private val generators: Map<BankCode, AccountNumberGenerator>,
  private val bankAccountRepository: BankAccountRepository,
) {

  companion object {
    private const val CREATE_ACCOUNT_RETRY_COUNT = 3
  }

  fun generateUniqueBankCode(bankCode: BankCode): String {
    val generator = generators[bankCode]
      ?: throw IllegalArgumentException("지원하지 않는 은행 : $bankCode")

    repeat(CREATE_ACCOUNT_RETRY_COUNT) {
      val accountNumber = generator.generate()

      if(!bankAccountRepository.existByBankAccount(accountNumber)) {
        return accountNumber
      }
    }

    throw IllegalArgumentException("계좌번호 생성 실패")
  }

  fun format(accountNumber: String, bankCode: BankCode): String {
    val generator = generators[bankCode]
      ?: throw IllegalArgumentException("지원하지 않는 은행 : $bankCode")

    return generator.format(accountNumber)
  }


}

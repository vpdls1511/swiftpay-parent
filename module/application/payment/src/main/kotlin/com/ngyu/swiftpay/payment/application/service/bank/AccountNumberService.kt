package com.ngyu.swiftpay.payment.application.service.bank

import com.ngyu.swiftpay.core.domain.bank.BankCode
import com.ngyu.swiftpay.payment.application.service.bank.account.number.generator.AccountNumberGenerator
import org.springframework.stereotype.Service

@Service
class AccountNumberService(
  private val generators: Map<BankCode, AccountNumberGenerator>
) {

  fun generateUniqueBankCode(bankCode: BankCode): String {
    val generator = generators[bankCode] ?: throw IllegalArgumentException("BankCode $bankCode not found")

    return "TESTBANKCODE"
  }

}

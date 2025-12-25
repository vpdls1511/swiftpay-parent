package com.ngyu.swiftpay.payment.application.service.bank

import com.ngyu.swiftpay.core.domain.bank.BankAccount
import com.ngyu.swiftpay.core.port.repository.BankAccountRepository
import com.ngyu.swiftpay.payment.api.dto.BankAccountCreateDto
import com.ngyu.swiftpay.payment.api.dto.BankAccountResponseDto
import org.springframework.stereotype.Service

@Service
class BankService(
  private val accountNumberService: AccountNumberService,
  private val bankAccountRepository: BankAccountRepository
) {
  fun create(request: BankAccountCreateDto): BankAccountResponseDto {
    val accountNumber = accountNumberService.generateUniqueBankCode(request.bankCode)

    val newBankAccount = BankAccount.create(
      bankCode = request.bankCode,
      accountNumber = accountNumber,
      holder = request.holder
    )
    val savedBankAccount = bankAccountRepository.save(newBankAccount)

    val formattedAccountNumber = accountNumberService.format(
      accountNumber = savedBankAccount.accountNumber,
      bankCode = savedBankAccount.bankCode
    )

    return BankAccountResponseDto.fromDomain(savedBankAccount, formattedAccountNumber)
  }
}

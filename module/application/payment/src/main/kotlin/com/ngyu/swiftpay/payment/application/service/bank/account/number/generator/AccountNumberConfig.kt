package com.ngyu.swiftpay.payment.application.service.bank.account.number.generator

import com.ngyu.swiftpay.core.domain.bank.BankCode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccountNumberConfig {

  @Bean
  fun accountNumberGenerator(
    generators: List<AccountNumberGenerator>
  ): Map<BankCode, AccountNumberGenerator> {
    return generators.associateBy { it.supportedBankCode() }
  }
}

package com.ngyu.swiftpay.payment.application.generator.bank.account.number

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

package com.ngyu.swiftpay.payment.api.controller.dto

import com.ngyu.swiftpay.core.domain.bank.BankAccount
import com.ngyu.swiftpay.core.domain.bank.BankCode
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank

@Schema(description = "계좌번호 생성 DTO")
data class BankAccountCreateDto(

  @field:NotBlank(message = "예금주명은 필수입니다")
  @field:Size(min = 2, max = 100, message = "예금주명은 2~100자")
  val holder: String,
  val bankCode: BankCode
)


@Schema(description = "생성 완료된 계좌")
data class BankAccountResponseDto(
  val bankName: String,
  val accountNumber: String,
  val accountHolder: String
) {
  companion object {
    fun fromDomain(domain: BankAccount, accountNumber: String): BankAccountResponseDto {
      return BankAccountResponseDto(
        bankName = domain.bankCode.label,
        accountNumber = accountNumber,
        accountHolder = domain.accountHolder,
      )
    }
  }
}

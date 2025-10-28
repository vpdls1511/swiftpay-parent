package com.ngyu.swiftpay.infrastructure.db.generator

import com.ngyu.swiftpay.core.domain.bank.BankCode
import com.ngyu.swiftpay.core.port.AccountNumberGenerator
import com.ngyu.swiftpay.core.port.SequenceGenerator
import org.springframework.stereotype.Component

@Component
class SwiftBankAccountNumberGenerator(
  private val sequenceGenerator: SequenceGenerator
): AccountNumberGenerator {

  companion object {
    private const val BRANCH_CODE = "218"
    private const val PRODUCT_CODE = "20"
    private const val SEQUENCE_LENGTH = 10
    private const val ACCOUNT_LENGTH = 16
  }

  /**
   * 계좌번호 생성 (16자리)
   * @return 2182000000000012 형식의 계좌번호
   */
  override fun generate(): String {
    // 218 20 0000000001 2
    // BRANCH_CODE PRODUCT_CODE sequence CHECKSUM
    val sequence = sequenceGenerator.nextBankAccountNumber()
      .toString()
      .padStart(SEQUENCE_LENGTH, '0')

    val accountNumber = "$BRANCH_CODE$PRODUCT_CODE$sequence"
    val checksum = this.generateChecksum(accountNumber)

    return "$accountNumber$checksum"
  }

  /**
   * 계좌번호 유효성 검사
   * @return true/false
   */
  override fun validate(accountNumber: String): Boolean {
    if (accountNumber.length != ACCOUNT_LENGTH) return false
    if (!accountNumber.startsWith(BRANCH_CODE)) return false

    val base = accountNumber.substring(0, 15)
    val checksum = accountNumber.last().toString()

    return generateChecksum(base) == checksum
  }

  /**
   * 16자리 계좌번호를 계좌 Role에맞게 포맷팅
   * @return 218-20-0000000001-2 형태로 리턴
   */
  override fun format(accountNumber: String): String {
    require(accountNumber.length == 16) { "계좌번호는 16자리여야 합니다" }

    val branchCode = accountNumber.substring(0, 3)      // 218
    val productCode = accountNumber.substring(3, 5)     // 20
    val sequence = accountNumber.substring(5, 15)       // 0000000001
    val checksum = accountNumber.substring(15)          // 2

    return "$branchCode-$productCode-$sequence-$checksum"
  }

  override fun supportedBankCode(): BankCode {
    return BankCode.SWIFT
  }

  private fun generateChecksum(base: String): String {
    val sum = base.mapIndexed { index, char ->
      val digit = char.digitToInt()
      if (index % 2 == 0) digit * 2 else digit
    }.sum()

    return ((10 - (sum % 10)) % 10).toString()
  }
}

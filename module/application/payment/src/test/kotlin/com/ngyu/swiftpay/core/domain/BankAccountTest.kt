package com.ngyu.swiftpay.core.domain

import com.ngyu.swiftpay.core.domain.bank.BankCode
import com.ngyu.swiftpay.core.domain.port.SequenceGenerator
import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.payment.application.generator.bank.account.number.SwiftBankAccountNumberGenerator
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BankAccountTest {

  private val log = logger()

  private lateinit var sequenceGenerator: SequenceGenerator
  private lateinit var generator: SwiftBankAccountNumberGenerator

  @BeforeEach
  fun setUp() {
    sequenceGenerator = mockk()
    generator = SwiftBankAccountNumberGenerator(sequenceGenerator)
  }

  @Test
  @DisplayName("계좌번호를 생성할 수 있다")
  fun createAccountNumber() {
    // given
    every { sequenceGenerator.nextVal() } returns 1234L

    // when
    val accountNumber = generator.generate()
    log.debug("accountNumber : $accountNumber")

    // then
    assertThat(accountNumber).hasSize(16)
    assertThat(accountNumber).startsWith("21820")  // 218(지점) + 20(상품)
  }

  @Test
  @DisplayName("생성된 계좌번호는 체크섬 검증을 통과한다")
  fun validateGeneratedAccountNumber() {
    // given
    every { sequenceGenerator.nextVal() } returns 1234L

    // when
    val accountNumber = generator.generate()

    // then
    assertThat(generator.validate(accountNumber)).isTrue()
  }

  @Test
  @DisplayName("유효한 계좌번호는 검증을 통과한다")
  fun validateValidAccountNumber() {
    // given
    val validAccountNumber = "2182000000012341"

    // when & then
    assertThat(generator.validate(validAccountNumber)).isTrue()
  }

  @Test
  @DisplayName("길이가 16자리가 아니면 검증 실패한다")
  fun validateInvalidLength() {
    // given
    val shortAccountNumber = "218200000001"

    // when & then
    assertThat(generator.validate(shortAccountNumber)).isFalse()
  }

  @Test
  @DisplayName("지점코드가 다르면 검증 실패한다")
  fun validateInvalidBranchCode() {
    // given
    val invalidBranchCode = "9992000000012349"

    // when & then
    assertThat(generator.validate(invalidBranchCode)).isFalse()
  }

  @Test
  @DisplayName("체크섬이 틀리면 검증 실패한다")
  fun validateInvalidChecksum() {
    // given
    every { sequenceGenerator.nextVal() } returns 1234L
    val accountNumber = generator.generate()
    val invalidChecksum = accountNumber.dropLast(1) + "9"

    // when & then
    assertThat(generator.validate(invalidChecksum)).isFalse()
  }

  @Test
  @DisplayName("계좌번호를 포맷팅할 수 있다")
  fun formatAccountNumber() {
    // given
    every { sequenceGenerator.nextVal() } returns 1234L
    val accountNumber = generator.generate()

    // when
    val formatted = generator.format(accountNumber)

    // then
    assertThat(formatted).matches("\\d{3}-\\d{2}-\\d{10}-\\d{1}")  // 218-20-0000001234-X
    assertThat(formatted).startsWith("218-20-")
  }

  @Test
  @DisplayName("16자리가 아닌 계좌번호는 포맷팅 시 예외 발생")
  fun formatInvalidLength() {
    // given
    val invalidAccountNumber = "218200000001"

    // when & then
    val exception = assertThrows<IllegalArgumentException> {
      generator.format(invalidAccountNumber)
    }
    assertThat(exception.message).isEqualTo("계좌번호는 16자리여야 합니다")
  }

  @Test
  @DisplayName("지원하는 은행코드는 SWIFT다")
  fun supportedBankCode() {
    // when
    val bankCode = generator.supportedBankCode()

    // then
    assertThat(bankCode).isEqualTo(BankCode.SWIFT)
  }
}

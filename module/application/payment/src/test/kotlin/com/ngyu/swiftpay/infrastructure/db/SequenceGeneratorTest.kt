package com.ngyu.swiftpay.infrastructure.db

import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.port.SequenceGenerator
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SequenceGeneratorTest {

  private val log = logger()

  private lateinit var sequenceGenerator: SequenceGenerator

  @BeforeEach
  fun setUp() {
    sequenceGenerator = mockk()
  }

  @Test
  @DisplayName("Sequence에 따라서 paymentId가 자동 생성되어야함.")
  fun nextSequenceId() {
    //given
    every { sequenceGenerator.nextPaymentId() } returns 1234L

    //when
    val paymentId = sequenceGenerator.nextPaymentId()

    assertThat(paymentId).isEqualTo(1234L)
  }
}

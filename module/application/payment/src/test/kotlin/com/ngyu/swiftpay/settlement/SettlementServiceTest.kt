package com.ngyu.swiftpay.settlement

import com.ngyu.swiftpay.core.domain.escrow.Escrow
import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.PaymentCardType
import com.ngyu.swiftpay.core.domain.payment.PaymentMethod
import com.ngyu.swiftpay.core.domain.payment.vo.PaymentMethodDetails
import com.ngyu.swiftpay.core.domain.settlement.Settlement
import com.ngyu.swiftpay.core.domain.settlement.SettlementStatus
import com.ngyu.swiftpay.core.port.generator.SequenceGenerator
import com.ngyu.swiftpay.core.port.repository.SettlementRepository
import com.ngyu.swiftpay.core.vo.Currency
import com.ngyu.swiftpay.payment.application.service.settlement.SettlementService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class SettlementServiceTest {

  @Mock
  private lateinit var settlementRepository: SettlementRepository

  @Mock
  private lateinit var sequenceGenerator: SequenceGenerator

  @InjectMocks
  private lateinit var settlementService: SettlementService

  private lateinit var payment: Payment

  @BeforeEach
  fun setUp() {
    payment = Payment.create(
      paymentSeq = 1L,
      paymentId = Payment.createPaymentId(1L),
      merchantId = "pair_key_001",
      orderId = "ORDER_001",
      orderName = "나이키 에어포스",
      amount = BigDecimal("129000.00"),
      currency = Currency.KRW,
      method = PaymentMethod.CARD,
      methodDetail = PaymentMethodDetails.Card(
        cardNumber = "1234****5678",
        cardExpiry = "2512",
        cardCvc = "***",
        installmentPlan = 0,
        cardType = PaymentCardType.CREDIT,
        useCardPoint = false
      ),
      successUrl = "https://example.com/success",
      cancelUrl = "https://example.com/cancel",
      failureUrl = "https://example.com/fail",
      idempotencyKey = "idempotency_001"
    )
  }

  @DisplayName("예치 데이터가 정산 대기상태로 들어간다")
  @Test
  fun canSettledEscrowData() {
    // given
    val escrow = Escrow.hold(id = 1L, payment = payment)
    given(sequenceGenerator.nextSettlementId()).willReturn(1L)

    // when
    settlementService.pending(escrow, "TEST_MERCHANT_ID")

    // then
    verify(settlementRepository).save(any())
  }

  @DisplayName("정산 금액은 예치 금액과 동일하다")
  @Test
  fun settlementAmountShouldMatchEscrowAmount() {
    // given
    val escrow = Escrow.hold(id = 1L, payment = payment)
    given(sequenceGenerator.nextSettlementId()).willReturn(1L)
    val captor = argumentCaptor<Settlement>()

    // when
    settlementService.pending(escrow, "TEST_MERCHANT_ID")

    // then
    verify(settlementRepository).save(captor.capture())
    assertThat(captor.firstValue.totalAmount).isEqualTo(escrow.amount)
  }

  @DisplayName("생성된 정산 상태는 PENDING이다")
  @Test
  fun createdSettlementStatusShouldBePending() {
    // given
    val escrow = Escrow.hold(id = 1L, payment = payment)
    given(sequenceGenerator.nextSettlementId()).willReturn(1L)
    val captor = argumentCaptor<Settlement>()

    // when
    settlementService.pending(escrow, "TEST_MERCHANT_ID")

    // then
    verify(settlementRepository).save(captor.capture())
    assertThat(captor.firstValue.status).isEqualTo(SettlementStatus.PENDING)
  }

  @DisplayName("가상계좌 결제도 정산 대기상태로 전환된다")
  @Test
  fun virtualAccountPaymentCanBePending() {
    // given
    val vaPayment = Payment.create(
      paymentSeq = 2L,
      paymentId = Payment.createPaymentId(2L),
      merchantId = "pair_key_001",
      orderId = "ORDER_002",
      orderName = "아디다스 슈퍼스타",
      amount = BigDecimal("89000.00"),
      currency = Currency.KRW,
      method = PaymentMethod.BANK_TRANSFER,
      methodDetail = PaymentMethodDetails.BankTransfer(
        bankCode = "004",
        accountNumber = "12345678901234"
      ),
      successUrl = "https://example.com/success",
      cancelUrl = "https://example.com/cancel",
      failureUrl = "https://example.com/fail",
      idempotencyKey = "idempotency_002"
    )
    val escrow = Escrow.hold(id = 2L, payment = vaPayment)
    given(sequenceGenerator.nextSettlementId()).willReturn(2L)

    // when
    settlementService.pending(escrow, "TEST_MERCHANT_ID")

    // then
    verify(settlementRepository).save(any())
  }

  @DisplayName("merchantId가 정산 데이터에 올바르게 저장된다")
  @Test
  fun merchantIdShouldBeStoredCorrectly() {
    // given
    val merchantId = "TEST_MERCHANT_001"
    val escrow = Escrow.hold(id = 1L, payment = payment)
    given(sequenceGenerator.nextSettlementId()).willReturn(1L)
    val captor = argumentCaptor<Settlement>()

    // when
    settlementService.pending(escrow, merchantId)

    // then
    verify(settlementRepository).save(captor.capture())
    assertThat(captor.firstValue.merchantId).isEqualTo(merchantId)
  }
}

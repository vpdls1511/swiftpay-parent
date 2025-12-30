package com.ngyu.swiftpay.infrastructure.fake

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.PaymentCardType
import com.ngyu.swiftpay.core.domain.payment.PaymentMethod
import com.ngyu.swiftpay.core.domain.payment.PaymentStatus
import com.ngyu.swiftpay.core.domain.payment.vo.PaymentMethodDetails
import com.ngyu.swiftpay.core.vo.Currency
import com.ngyu.swiftpay.core.vo.Money
import com.ngyu.swiftpay.infrastructure.db.persistent.payment.mapper.PaymentMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PaymentEntityTest {

  private lateinit var fakePaymentJpaRepository: FakePaymentJpaRepository

  @BeforeEach
  fun setup() {
    fakePaymentJpaRepository = FakePaymentJpaRepository()
  }

  @Test
  @DisplayName("카드 결제 도메인 생성 및 저장 후 조회")
  fun `카드 결제 도메인을 생성하고 저장한 후 다시 조회할 수 있다`() {
    // given
    val paymentSeq = 1L
    val paymentId = Payment.createPaymentId(paymentSeq)

    val payment = Payment.create(
      paymentSeq = paymentSeq,
      paymentId = paymentId,
      merchantId = 1L,
      orderId = 1L,
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

    // when
    val entity = PaymentMapper.toEntity(payment)
    val saved = fakePaymentJpaRepository.save(entity)

    val foundEntity = fakePaymentJpaRepository.findById(saved.id!!).get()
    val foundDomain = PaymentMapper.toDomain(foundEntity)

    // then
    assertEquals(payment.paymentId, foundDomain.paymentId)
    assertEquals(1, foundDomain.merchantId)
    assertEquals(1, foundDomain.orderId)
    assertEquals("나이키 에어포스", foundDomain.orderName)
    assertEquals(Money.won(129000), foundDomain.amount)
    assertEquals(PaymentMethod.CARD, foundDomain.method)
    assertEquals(PaymentStatus.PENDING, foundDomain.status)
    assertEquals("idempotency_001", foundDomain.idempotencyKey)

    val cardDetail = foundDomain.methodDetail as PaymentMethodDetails.Card
    assertEquals("1234****5678", cardDetail.cardNumber)
    assertEquals(PaymentCardType.CREDIT, cardDetail.cardType)
    assertNotNull(foundDomain.createdAt)
  }

  @Test
  @DisplayName("계좌이체 결제 도메인 생성 및 저장 후 조회")
  fun `계좌이체 결제 도메인을 생성하고 저장한 후 다시 조회할 수 있다`() {
    // given
    val paymentSeq = 1L
    val paymentId = Payment.createPaymentId(paymentSeq)

    val payment = Payment.create(
      paymentSeq = paymentSeq,
      paymentId = paymentId,
      merchantId = 1L,
      orderId = 1L,
      orderName = "아디다스 슈퍼스타",
      amount = BigDecimal("99000.00"),
      currency = Currency.KRW,
      method = PaymentMethod.BANK_TRANSFER,
      methodDetail = PaymentMethodDetails.BankTransfer(
        bankCode = "004",
        accountNumber = "110-123-456789"
      ),
      successUrl = "https://example.com/success",
      cancelUrl = "https://example.com/cancel",
      failureUrl = "https://example.com/fail",
      idempotencyKey = "idempotency_002"
    )

    // when
    val entity = PaymentMapper.toEntity(payment)
    val saved = fakePaymentJpaRepository.save(entity)

    val foundEntity = fakePaymentJpaRepository.findById(saved.id!!).get()
    val foundDomain = PaymentMapper.toDomain(foundEntity)

    // then
    assertEquals(PaymentMethod.BANK_TRANSFER, foundDomain.method)
    assertTrue(foundDomain.methodDetail is PaymentMethodDetails.BankTransfer)
    val bankDetail = foundDomain.methodDetail as PaymentMethodDetails.BankTransfer
    assertEquals("004", bankDetail.bankCode)
    assertEquals("110-123-456789", bankDetail.accountNumber)
  }

  @Test
  @DisplayName("할부가 있는 신용카드 결제")
  fun `할부가 있는 신용카드 결제를 저장하고 조회할 수 있다`() {
    // given
    val paymentSeq = 1L
    val paymentId = Payment.createPaymentId(paymentSeq)

    val payment = Payment.create(
      paymentSeq = paymentSeq,
      paymentId = paymentId,
      merchantId = 1L,
      orderId = 1L,
      orderName = "맥북 프로",
      amount = BigDecimal("2500000.00"),
      currency = Currency.KRW,
      method = PaymentMethod.CARD,
      methodDetail = PaymentMethodDetails.Card(
        cardNumber = "9410****1234",
        cardExpiry = "2612",
        cardCvc = "***",
        installmentPlan = 12,
        cardType = PaymentCardType.CREDIT,
        useCardPoint = true
      ),
      successUrl = "https://example.com/success",
      cancelUrl = "https://example.com/cancel",
      failureUrl = "https://example.com/fail",
      idempotencyKey = "idempotency_003"
    )

    // when
    val entity = PaymentMapper.toEntity(payment)
    val saved = fakePaymentJpaRepository.save(entity)

    val foundEntity = fakePaymentJpaRepository.findById(saved.id!!).get()
    val foundDomain = PaymentMapper.toDomain(foundEntity)

    // then
    val cardDetail = foundDomain.methodDetail as PaymentMethodDetails.Card
    assertEquals(12, cardDetail.installmentPlan)
    assertEquals(true, cardDetail.useCardPoint)
  }

  @Test
  @DisplayName("체크카드 일시불 결제")
  fun `체크카드 일시불 결제를 저장하고 조회할 수 있다`() {
    // given
    val payment = createTestCardPayment("ORDER_004", cardType = PaymentCardType.DEBIT)

    // when
    val entity = PaymentMapper.toEntity(payment)
    val saved = fakePaymentJpaRepository.save(entity)

    val foundEntity = fakePaymentJpaRepository.findById(saved.id!!).get()
    val foundDomain = PaymentMapper.toDomain(foundEntity)

    // then
    val cardDetail = foundDomain.methodDetail as PaymentMethodDetails.Card
    assertEquals(PaymentCardType.DEBIT, cardDetail.cardType)
  }

  @Test
  @DisplayName("nullable 필드들이 null인 경우")
  fun `선택적 필드들이 null로 저장되고 조회된다`() {
    // given
    val paymentSeq = 1L
    val paymentId = Payment.createPaymentId(paymentSeq)

    val payment = Payment.create(
      paymentSeq = paymentSeq,
      paymentId = paymentId,
      merchantId = 1L,
      orderId = 1L,
      orderName = "테스트 상품",
      amount = BigDecimal("10000.00"),
      currency = Currency.KRW,
      method = PaymentMethod.CARD,
      methodDetail = PaymentMethodDetails.Card(
        cardNumber = "1231231231",
        cardExpiry = "1212",
        cardCvc = "122",
        installmentPlan = 2,
        cardType = PaymentCardType.CREDIT,
        useCardPoint = false
      ),
      successUrl = null,
      cancelUrl = null,
      failureUrl = null,
      idempotencyKey = null
    )

    // when
    val entity = PaymentMapper.toEntity(payment)
    val saved = fakePaymentJpaRepository.save(entity)

    val foundEntity = fakePaymentJpaRepository.findById(saved.id!!).get()
    val foundDomain = PaymentMapper.toDomain(foundEntity)

    // then
    val cardDetail = foundDomain.methodDetail as PaymentMethodDetails.Card
    assertNull(foundDomain.successUrl)
  }

  @Test
  @DisplayName("초기 결제 상태는 PENDING")
  fun `생성된 결제의 초기 상태는 PENDING이다`() {
    // given
    val payment = createTestCardPayment("ORDER_011")

    // then
    assertEquals(PaymentStatus.PENDING, payment.status)
  }

  @Test
  @DisplayName("결제 삭제")
  fun `결제를 삭제할 수 있다`() {
    // given
    val payment = createTestCardPayment("ORDER_012")
    val entity = PaymentMapper.toEntity(payment)
    val saved = fakePaymentJpaRepository.save(entity)

    // when
    fakePaymentJpaRepository.deleteById(saved.id!!)

    // then
    assertFalse(fakePaymentJpaRepository.existsById(saved.id!!))
  }

  @Test
  @DisplayName("결제 존재 여부 확인")
  fun `결제 존재 여부를 확인할 수 있다`() {
    // given
    val payment = createTestCardPayment("ORDER_013")
    val entity = PaymentMapper.toEntity(payment)
    val saved = fakePaymentJpaRepository.save(entity)

    // when & then
    assertTrue(fakePaymentJpaRepository.existsById(saved.id!!))
    assertFalse(fakePaymentJpaRepository.existsById(999L))
  }

  private fun createTestCardPayment(
    orderId: String,
    idempotencyKey: String? = "idempotency_$orderId",
    cardType: PaymentCardType = PaymentCardType.CREDIT
  ): Payment {
    val paymentSeq = 1L
    val paymentId = Payment.createPaymentId(paymentSeq)

    return Payment.create(
      paymentSeq = paymentSeq,
      paymentId = paymentId,
      merchantId = 1L,
      orderId = 1L,
      orderName = "테스트 상품",
      amount = BigDecimal("10000.00"),
      currency = Currency.KRW,
      method = PaymentMethod.CARD,
      methodDetail = PaymentMethodDetails.Card(
        cardNumber = "1234****5678",
        cardExpiry = "2512",
        cardCvc = "123",
        installmentPlan = 0,
        cardType = cardType,
        useCardPoint = false
      ),
      successUrl = "https://example.com/success",
      cancelUrl = "https://example.com/cancel",
      failureUrl = "https://example.com/fail",
      idempotencyKey = idempotencyKey
    )
  }

  private fun createTestBankTransferPayment(
    orderId: String,
    idempotencyKey: String = "idempotency_$orderId"
  ): Payment {
    val paymentSeq = 1L
    val paymentId = Payment.createPaymentId(paymentSeq)

    return Payment.create(
      paymentSeq = paymentSeq,
      paymentId = paymentId,
      merchantId = 1L,
      orderId = 1L,
      orderName = "테스트 상품",
      amount = BigDecimal("10000.00"),
      currency = Currency.KRW,
      method = PaymentMethod.BANK_TRANSFER,
      methodDetail = PaymentMethodDetails.BankTransfer(
        bankCode = "004",
        accountNumber = "110-123-456789"
      ),
      successUrl = "https://example.com/success",
      cancelUrl = "https://example.com/cancel",
      failureUrl = "https://example.com/fail",
      idempotencyKey = idempotencyKey
    )
  }
}

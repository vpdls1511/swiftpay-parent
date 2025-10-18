package com.ngyu.swiftpay.infrastructure.fake

import com.ngyu.swiftpay.core.domain.money.Currency
import com.ngyu.swiftpay.core.domain.payment.model.*
import com.ngyu.swiftpay.infrastructure.db.persistent.payment.mapper.PaymentMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * ### PaymentEntity 통합 테스트
 *
 * FakeRepository를 사용한 도메인 저장 및 조회 테스트
 */
class PaymentEntityTest {

  private lateinit var fakePaymentJpaRepository: FakePaymentJpaRepository

  @BeforeEach
  fun setup() {
    fakePaymentJpaRepository = FakePaymentJpaRepository()
  }

  @Test
  @DisplayName("카드 결제 도메인 생성 및 저장 후 조회")
  fun `카드 결제 도메인을 생성하고 저장한 후 다시 조회할 수 있다`() {
    // given - 도메인 생성
    val payment = Payment.create(
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

    // when - Entity로 변환 후 저장
    val entity = PaymentMapper.toEntity(payment)
    fakePaymentJpaRepository.save(entity)

    // Entity 조회 후 도메인으로 변환
    val foundEntity = fakePaymentJpaRepository.findById(entity.id).get()
    val foundDomain = PaymentMapper.toDomain(foundEntity)

    // then - 도메인 검증
    assertEquals(payment.id, foundDomain.id)
    assertEquals("pair_key_001", foundDomain.merchantId)
    assertEquals("ORDER_001", foundDomain.orderId)
    assertEquals("나이키 에어포스", foundDomain.orderName)
    assertEquals(BigDecimal("129000.00"), foundDomain.amount)
    assertEquals(PaymentMethod.CARD, foundDomain.method)
    assertEquals(PaymentStatus.PENDING, foundDomain.status)
    assertEquals("idempotency_001", foundDomain.idempotencyKey)
    assertEquals("https://example.com/success", foundDomain.successUrl)
    assertEquals("https://example.com/cancel", foundDomain.cancelUrl)
    assertEquals("https://example.com/fail", foundDomain.failureUrl)

    // 결제 수단 상세 검증
    assertTrue(foundDomain.methodDetail is PaymentMethodDetails.Card)
    val cardDetail = foundDomain.methodDetail as PaymentMethodDetails.Card
    assertEquals("1234****5678", cardDetail.cardNumber)
    assertEquals("2512", cardDetail.cardExpiry)
    assertEquals("***", cardDetail.cardCvc)
    assertEquals(PaymentCardType.CREDIT, cardDetail.cardType)
    assertEquals(0, cardDetail.installmentPlan)
    assertEquals(false, cardDetail.useCardPoint)

    // 시스템 정보 검증
    assertNotNull(foundDomain.createdAt)
    assertNotNull(foundDomain.updatedAt)
  }

  @Test
  @DisplayName("계좌이체 결제 도메인 생성 및 저장 후 조회")
  fun `계좌이체 결제 도메인을 생성하고 저장한 후 다시 조회할 수 있다`() {
    // given
    val payment = Payment.create(
      merchantId = "pair_key_001",
      orderId = "ORDER_002",
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
    fakePaymentJpaRepository.save(entity)

    val foundEntity = fakePaymentJpaRepository.findById(entity.id).get()
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
    val payment = Payment.create(
      merchantId = "pair_key_001",
      orderId = "ORDER_003",
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
    fakePaymentJpaRepository.save(entity)

    val foundEntity = fakePaymentJpaRepository.findById(entity.id).get()
    val foundDomain = PaymentMapper.toDomain(foundEntity)

    // then
    val cardDetail = foundDomain.methodDetail as PaymentMethodDetails.Card
    assertEquals(12, cardDetail.installmentPlan)
    assertEquals(true, cardDetail.useCardPoint)
    assertEquals(PaymentCardType.CREDIT, cardDetail.cardType)
  }

  @Test
  @DisplayName("체크카드 일시불 결제")
  fun `체크카드 일시불 결제를 저장하고 조회할 수 있다`() {
    // given
    val payment = Payment.create(
      merchantId = "pair_key_001",
      orderId = "ORDER_004",
      orderName = "스타벅스 아메리카노",
      amount = BigDecimal("4500.00"),
      currency = Currency.KRW,
      method = PaymentMethod.CARD,
      methodDetail = PaymentMethodDetails.Card(
        cardNumber = "9411****5678",
        cardExpiry = "2512",
        cardCvc = "123",
        installmentPlan = 0,
        cardType = PaymentCardType.DEBIT,
        useCardPoint = false
      ),
      successUrl = "https://example.com/success",
      cancelUrl = "https://example.com/cancel",
      failureUrl = "https://example.com/fail",
      idempotencyKey = "idempotency_004"
    )

    // when
    val entity = PaymentMapper.toEntity(payment)
    fakePaymentJpaRepository.save(entity)

    val foundEntity = fakePaymentJpaRepository.findById(entity.id).get()
    val foundDomain = PaymentMapper.toDomain(foundEntity)

    // then
    val cardDetail = foundDomain.methodDetail as PaymentMethodDetails.Card
    assertEquals(PaymentCardType.DEBIT, cardDetail.cardType)
    assertEquals(0, cardDetail.installmentPlan)
    assertEquals(false, cardDetail.useCardPoint)
  }

  @Test
  @DisplayName("nullable 필드들이 null인 경우")
  fun `선택적 필드들이 null로 저장되고 조회된다`() {
    // given
    val payment = Payment.create(
      merchantId = "pair_key_001",
      orderId = "ORDER_005",
      orderName = "테스트 상품",
      amount = BigDecimal("10000.00"),
      currency = Currency.KRW,
      method = PaymentMethod.CARD,
      methodDetail = PaymentMethodDetails.Card(
        cardNumber = null,
        cardExpiry = null,
        cardCvc = null,
        installmentPlan = null,
        cardType = null,
        useCardPoint = false
      ),
      successUrl = null,
      cancelUrl = null,
      failureUrl = null,
      idempotencyKey = null
    )

    // when
    val entity = PaymentMapper.toEntity(payment)
    fakePaymentJpaRepository.save(entity)

    val foundEntity = fakePaymentJpaRepository.findById(entity.id).get()
    val foundDomain = PaymentMapper.toDomain(foundEntity)

    // then
    val cardDetail = foundDomain.methodDetail as PaymentMethodDetails.Card
    assertNull(cardDetail.cardNumber)
    assertNull(cardDetail.cardExpiry)
    assertNull(cardDetail.cardCvc)
    assertNull(cardDetail.installmentPlan)
    assertNull(cardDetail.cardType)
    assertNull(foundDomain.successUrl)
    assertNull(foundDomain.cancelUrl)
    assertNull(foundDomain.failureUrl)
    assertNull(foundDomain.idempotencyKey)
  }

  @Test
  @DisplayName("여러 결제 도메인 일괄 저장")
  fun `여러 결제를 한 번에 저장할 수 있다`() {
    // given
    val payment1 = createTestCardPayment("ORDER_006")
    val payment2 = createTestCardPayment("ORDER_007")
    val payment3 = createTestBankTransferPayment("ORDER_008")

    // when
    val entities = listOf(payment1, payment2, payment3).map { PaymentMapper.toEntity(it) }
    fakePaymentJpaRepository.saveAll(entities)

    // then
    val allPayments = fakePaymentJpaRepository.findAll()
    assertEquals(3, allPayments.size)
  }

  @Test
  @DisplayName("결제 ID 자동 생성 확인")
  fun `결제 ID가 swift_pay 형식으로 자동 생성된다`() {
    // given
    val payment = createTestCardPayment("ORDER_009")

    // when
    val entity = PaymentMapper.toEntity(payment)
    fakePaymentJpaRepository.save(entity)

    // then
    assertTrue(payment.id.startsWith("swift_pay_"))
    val parts = payment.id.split("_")
    assertEquals(4, parts.size)
    assertEquals("swift", parts[0])
    assertEquals("pay", parts[1])
  }

  @Test
  @DisplayName("다양한 통화로 결제 저장")
  fun `USD 통화로 결제를 저장할 수 있다`() {
    // given
    val payment = Payment.create(
      merchantId = "pair_key_001",
      orderId = "ORDER_010",
      orderName = "International Product",
      amount = BigDecimal("99.99"),
      currency = Currency.KRW,
      method = PaymentMethod.CARD,
      methodDetail = PaymentMethodDetails.Card(
        cardNumber = "4111****1111",
        cardExpiry = "2512",
        cardCvc = "123",
        installmentPlan = 0,
        cardType = PaymentCardType.CREDIT,
        useCardPoint = false
      ),
      successUrl = "https://example.com/success",
      cancelUrl = "https://example.com/cancel",
      failureUrl = "https://example.com/fail",
      idempotencyKey = "idempotency_010"
    )

    // when
    val entity = PaymentMapper.toEntity(payment)
    fakePaymentJpaRepository.save(entity)

    val foundEntity = fakePaymentJpaRepository.findById(entity.id).get()
    val foundDomain = PaymentMapper.toDomain(foundEntity)

    // then
    assertEquals(BigDecimal("99.99"), foundDomain.amount)
  }

  @Test
  @DisplayName("초기 결제 상태는 PENDING")
  fun `생성된 결제의 초기 상태는 PENDING이다`() {
    // given
    val payment = createTestCardPayment("ORDER_011")

    // when
    val entity = PaymentMapper.toEntity(payment)
    fakePaymentJpaRepository.save(entity)

    val foundEntity = fakePaymentJpaRepository.findById(entity.id).get()
    val foundDomain = PaymentMapper.toDomain(foundEntity)

    // then
    assertEquals(PaymentStatus.PENDING, foundDomain.status)
  }

  @Test
  @DisplayName("결제 삭제")
  fun `결제를 삭제할 수 있다`() {
    // given
    val payment = createTestCardPayment("ORDER_012")
    val entity = PaymentMapper.toEntity(payment)
    fakePaymentJpaRepository.save(entity)

    // when
    fakePaymentJpaRepository.deleteById(entity.id)

    // then
    val found = fakePaymentJpaRepository.findById(entity.id)
    assertFalse(found.isPresent)
  }

  @Test
  @DisplayName("결제 존재 여부 확인")
  fun `결제 존재 여부를 확인할 수 있다`() {
    // given
    val payment = createTestCardPayment("ORDER_013")
    val entity = PaymentMapper.toEntity(payment)
    fakePaymentJpaRepository.save(entity)

    // when
    val exists = fakePaymentJpaRepository.existsById(entity.id)
    val notExists = fakePaymentJpaRepository.existsById("not_exist_id")

    // then
    assertTrue(exists)
    assertFalse(notExists)
  }

  @Test
  @DisplayName("전체 결제 개수 조회")
  fun `저장된 전체 결제 개수를 조회할 수 있다`() {
    // given
    val payment1 = createTestCardPayment("ORDER_014")
    val payment2 = createTestCardPayment("ORDER_015")
    val payment3 = createTestBankTransferPayment("ORDER_016")

    fakePaymentJpaRepository.save(PaymentMapper.toEntity(payment1))
    fakePaymentJpaRepository.save(PaymentMapper.toEntity(payment2))
    fakePaymentJpaRepository.save(PaymentMapper.toEntity(payment3))

    // when
    val count = fakePaymentJpaRepository.count()

    // then
    assertEquals(3, count)
  }

  // ========================================
  // 헬퍼 메서드
  // ========================================

  private fun createTestCardPayment(
    orderId: String,
    idempotencyKey: String? = "idempotency_$orderId",
    apiPairKey: String = "pair_key_001"
  ): Payment {
    return Payment.create(
      merchantId = apiPairKey,
      orderId = orderId,
      orderName = "테스트 상품",
      amount = BigDecimal("10000.00"),
      currency = Currency.KRW,
      method = PaymentMethod.CARD,
      methodDetail = PaymentMethodDetails.Card(
        cardNumber = "1234****5678",
        cardExpiry = "2512",
        cardCvc = "123",
        installmentPlan = 0,
        cardType = PaymentCardType.CREDIT,
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
    idempotencyKey: String = "idempotency_$orderId",
    apiPairKey: String = "pair_key_001"
  ): Payment {
    return Payment.create(
      merchantId = apiPairKey,
      orderId = orderId,
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

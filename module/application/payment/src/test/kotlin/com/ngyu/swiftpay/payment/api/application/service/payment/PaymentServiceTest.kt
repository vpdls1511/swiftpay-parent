package com.ngyu.swiftpay.payment.api.application.service.payment

import com.ngyu.swiftpay.core.domain.money.Currency
import com.ngyu.swiftpay.core.domain.payment.model.PayMethod
import com.ngyu.swiftpay.core.domain.payment.model.PayMethodDetails
import com.ngyu.swiftpay.core.domain.payment.model.PayStatus
import com.ngyu.swiftpay.core.domain.payment.model.Payment
import com.ngyu.swiftpay.core.domain.payment.port.PaymentRepository
import com.ngyu.swiftpay.payment.api.application.strategy.PaymentBankStrategy
import com.ngyu.swiftpay.payment.api.application.strategy.PaymentCardStrategy
import com.ngyu.swiftpay.payment.api.application.strategy.PaymentStrategyFactory
import com.ngyu.swiftpay.payment.api.dto.OrderCreateRequestDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
@DisplayName("PaymentService 테스트")
class PaymentServiceTest {

 @Mock
 private lateinit var paymentStrategyFactory: PaymentStrategyFactory

 @Mock
 private lateinit var paymentRepository: PaymentRepository

 @Mock
 private lateinit var mockCardStrategy: PaymentCardStrategy

 @Mock
 private lateinit var mockBankStrategy: PaymentBankStrategy

 @InjectMocks
 private lateinit var paymentService: PaymentService

 private lateinit var testPayment: Payment

 @BeforeEach
 fun setup() {
  testPayment = PaymentTestFixture.createPayment()
 }

 @Test
 @DisplayName("주문서 생성 - 성공")
 fun `주문서를 생성하면 OrderCreateResponseDto를 반환한다`() {
  // given
  val request = OrderCreateRequestDto(
   merchantId = "merchant_123",
   orderName = "테스트 상품",
   totalAmount = 10000L,
   currency = Currency.KRW,
   customerName = "홍길동",
   customerEmail = "test@example.com",
   customerPhone = "01012345678"
  )

  // when
  val result = paymentService.readyOrder(request)

  // then
  assertThat(result).isNotNull
  assertThat(result.orderId).isNotBlank()
  assertThat(result.orderName).isEqualTo("테스트 상품")
  assertThat(result.amount).isEqualTo(10000L)
  assertThat(result.customerName).isEqualTo("홍길동")
 }

 @Test
 @DisplayName("카드 결제 처리 - 동기 처리 선택")
 fun `카드 결제는 동기 처리를 선택한다`() {
  // given
  val request = PaymentTestFixture.createCardPaymentRequest(
   orderId = "order_123"
  )

  // Mock 설정 - 카드 전략 사용
  whenever(paymentRepository.save(any())).thenReturn(testPayment)
  whenever(paymentStrategyFactory.getStrategy(any())).thenReturn(mockCardStrategy)
//  whenever(mockCardStrategy.shouldAsyncProcessing(any())).thenReturn(false)
//  whenever(mockCardStrategy.getStrategyName()).thenReturn("카드 결제")

  // when
  val result = paymentService.processing(request)

  // then
  assertThat(result).isNotNull
  assertThat(result.trnKey).isNotBlank()

  // 검증
  verify(paymentRepository, times(1)).save(any())
  verify(paymentStrategyFactory, times(1)).getStrategy(any())
//  verify(mockCardStrategy, times(1)).shouldAsyncProcessing(any())
 }

 @Test
 @DisplayName("계좌이체 결제 처리 - 비동기 처리 선택")
 fun `계좌이체는 비동기 처리를 선택한다`() {
  // given
  val request = PaymentTestFixture.createBankTransferPaymentRequest(
   orderId = "order_123"
  )

  // Mock 설정 - 계좌이체 전략 사용
  whenever(paymentRepository.save(any())).thenReturn(testPayment)
  whenever(paymentStrategyFactory.getStrategy(any())).thenReturn(mockBankStrategy)
//  whenever(mockBankStrategy.shouldAsyncProcessing(any())).thenReturn(true)
//  whenever(mockBankStrategy.getStrategyName()).thenReturn("계좌이체")

  // when
  val result = paymentService.processing(request)

  // then
  assertThat(result).isNotNull

  // 검증
  verify(paymentRepository, times(1)).save(any())
  verify(paymentStrategyFactory, times(1)).getStrategy(any())
//  verify(mockBankStrategy, times(1)).shouldAsyncProcessing(any())
 }

 @Test
 @DisplayName("결제 정보 저장 - Payment 상태가 IN_PROGRESS로 변경")
 fun `결제 정보를 저장하면 상태가 IN_PROGRESS로 변경된다`() {
  // given
  val request = PaymentTestFixture.createCardPaymentRequest(
   orderId = "order_123"
  )

  // ArgumentCaptor 사용
  val paymentCaptor = argumentCaptor<Payment>()

  whenever(paymentRepository.save(paymentCaptor.capture())).thenReturn(testPayment)
  whenever(paymentStrategyFactory.getStrategy(any())).thenReturn(mockCardStrategy)
//  whenever(mockCardStrategy.shouldAsyncProcessing(any())).thenReturn(false)
//  whenever(mockCardStrategy.getStrategyName()).thenReturn("카드 결제")

  // when
  paymentService.processing(request)

  // then
  val savedPayment = paymentCaptor.firstValue
  assertThat(savedPayment.status).isEqualTo(PayStatus.IN_PROGRESS)

  verify(paymentRepository).save(any())
 }

 @Test
 @DisplayName("결제 수단별 전략 선택 - 카드")
 fun `카드 결제는 카드 전략이 선택된다`() {
  // given
  val request = PaymentTestFixture.createCardPaymentRequest(
   orderId = "order_123"
  )

  whenever(paymentRepository.save(any())).thenReturn(testPayment)
  whenever(paymentStrategyFactory.getStrategy(any())).thenReturn(mockCardStrategy)
//  whenever(mockCardStrategy.shouldAsyncProcessing(any())).thenReturn(false)
//  whenever(mockCardStrategy.getStrategyName()).thenReturn("카드 결제")

  // when
  paymentService.processing(request)

  // then - PayMethod.CARD로 전략 선택 확인
  verify(paymentStrategyFactory).getStrategy(argThat { payment ->
   payment.method == PayMethod.CARD
  })
 }

 @Test
 @DisplayName("결제 수단별 전략 선택 - 계좌이체")
 fun `계좌이체는 계좌이체 전략이 선택된다`() {
  // given
  val request = PaymentTestFixture.createBankTransferPaymentRequest(
   orderId = "order_123"
  )

  whenever(paymentRepository.save(any())).thenReturn(testPayment)
  whenever(paymentStrategyFactory.getStrategy(any())).thenReturn(mockBankStrategy)
//  whenever(mockBankStrategy.shouldAsyncProcessing(any())).thenReturn(true)
//  whenever(mockBankStrategy.getStrategyName()).thenReturn("계좌이체")

  // when
  paymentService.processing(request)

  // then - PayMethod.BANK_TRANSFER로 전략 선택 확인
  verify(paymentStrategyFactory).getStrategy(argThat { payment ->
   payment.method == PayMethod.BANK_TRANSFER
  })
 }

 @Test
 @DisplayName("결제 응답 검증 - PaymentResponseDto")
 fun `결제 처리 후 올바른 PaymentResponseDto를 반환한다`() {
  // given
  val request = PaymentTestFixture.createCardPaymentRequest(
   merchantId = "merchant_123",
   orderId = "order_123",
   amount = BigDecimal(10000)
  )

  whenever(paymentRepository.save(any())).thenReturn(testPayment)
  whenever(paymentStrategyFactory.getStrategy(any())).thenReturn(mockCardStrategy)
//  whenever(mockCardStrategy.shouldAsyncProcessing(any())).thenReturn(false)
//  whenever(mockCardStrategy.getStrategyName()).thenReturn("카드 결제")

  // when
  val result = paymentService.processing(request)

  // then
  assertThat(result.trnKey).isNotBlank()
  assertThat(result.orderId).isEqualTo("order_123")
  assertThat(result.merchantId).isEqualTo("merchant_123")
  assertThat(result.amount).isEqualTo(BigDecimal(10000))
 }

 @Test
 @DisplayName("카드 결제 - 할부 옵션")
 fun `할부 카드 결제가 정상 처리된다`() {
  // given
  val request = PaymentTestFixture.createCardPaymentRequest(
   orderId = "order_123",
   amount = BigDecimal(1000000),
   installmentPlan = 12
  )

  whenever(paymentRepository.save(any())).thenReturn(testPayment)
  whenever(paymentStrategyFactory.getStrategy(any())).thenReturn(mockCardStrategy)
//  whenever(mockCardStrategy.shouldAsyncProcessing(any())).thenReturn(false)
//  whenever(mockCardStrategy.getStrategyName()).thenReturn("카드 결제")

  // when
  val result = paymentService.processing(request)

  // then
  assertThat(result).isNotNull
  verify(paymentRepository).save(argThat { payment ->
   payment.methodDetail is PayMethodDetails.Card &&
       (payment.methodDetail as PayMethodDetails.Card).installmentPlan == 12
  })
 }

 @Test
 @DisplayName("계좌이체 - 특정 은행")
 fun `신한은행 계좌이체가 정상 처리된다`() {
  // given
  val request = PaymentTestFixture.createBankTransferPaymentRequest(
   orderId = "order_123",
   bankCode = "088",
   accountNumber = "110-123-456789"
  )

  whenever(paymentRepository.save(any())).thenReturn(testPayment)
  whenever(paymentStrategyFactory.getStrategy(any())).thenReturn(mockBankStrategy)
//  whenever(mockBankStrategy.shouldAsyncProcessing(any())).thenReturn(true)
//  whenever(mockBankStrategy.getStrategyName()).thenReturn("계좌이체")

  // when
  val result = paymentService.processing(request)

  // then
  assertThat(result).isNotNull
  verify(paymentRepository).save(argThat { payment ->
   payment.methodDetail is PayMethodDetails.BankTransfer &&
       (payment.methodDetail as PayMethodDetails.BankTransfer).bankCode == "088"
  })
 }
}

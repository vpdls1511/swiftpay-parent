package com.ngyu.swiftpay.payment.api.application.service.payment

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.PaymentCardType
import com.ngyu.swiftpay.core.domain.payment.PaymentMethod
import com.ngyu.swiftpay.core.domain.payment.PaymentStatus
import com.ngyu.swiftpay.core.domain.payment.vo.PaymentMethodDetails
import com.ngyu.swiftpay.core.vo.Currency
import com.ngyu.swiftpay.payment.api.controller.dto.PaymentCallback
import com.ngyu.swiftpay.payment.api.controller.dto.PaymentDtoMethodDetails
import com.ngyu.swiftpay.payment.api.controller.dto.PaymentRequestDto
import java.math.BigDecimal

/**
 * 결제 테스트용 Fixture
 */
object PaymentTestFixture {

  private val paymentSeq = 1L
  private val paymentId = Payment.createPaymentId(paymentSeq)

  /**
   * 기본 카드 결제 요청
   */
  fun createCardPaymentRequest(
    merchantId: String = "merchant_123",
    orderId: String = "order_${System.currentTimeMillis()}",
    orderName: String = "테스트 상품",
    amount: BigDecimal = BigDecimal(10000),
    currency: Currency = Currency.KRW,
    cardNumber: String = "1234567890123456",
    cardExpiry: String = "12/25",
    cardCvc: String = "123",
    installmentPlan: Int = 0,
    cardType: PaymentCardType = PaymentCardType.CREDIT,
    useCardPoint: Boolean = false,
    successUrl: String? = null,
    cancelUrl: String? = null,
    failureUrl: String? = null
  ): PaymentRequestDto {
    return PaymentRequestDto(
      merchantId = merchantId,
      orderId = orderId,
      orderName = orderName,
      amount = amount,
      currency = currency,
      method = PaymentMethod.CARD,
      methodDetail = PaymentDtoMethodDetails.Card(
        cardNumber = cardNumber,
        cardExpiry = cardExpiry,
        cardCvc = cardCvc,
        installmentPlan = installmentPlan,
        cardType = cardType,
        useCardPoint = useCardPoint
      ),
      callBack = if (successUrl != null || cancelUrl != null || failureUrl != null) {
        PaymentCallback(successUrl, cancelUrl, failureUrl)
      } else null
    )
  }

  /**
   * 기본 계좌이체 결제 요청
   */
  fun createBankTransferPaymentRequest(
    merchantId: String = "merchant_123",
    orderId: String = "order_${System.currentTimeMillis()}",
    orderName: String = "테스트 상품",
    amount: BigDecimal = BigDecimal(10000),
    currency: Currency = Currency.KRW,
    bankCode: String = "004",
    accountNumber: String = "1234567890",
    successUrl: String? = null,
    cancelUrl: String? = null,
    failureUrl: String? = null
  ): PaymentRequestDto {
    return PaymentRequestDto(
      merchantId = merchantId,
      orderId = orderId,
      orderName = orderName,
      amount = amount,
      currency = currency,
      method = PaymentMethod.BANK_TRANSFER,
      methodDetail = PaymentDtoMethodDetails.BankTransfer(
        bankCode = bankCode,
        accountNumber = accountNumber
      ),
      callBack = if (successUrl != null || cancelUrl != null || failureUrl != null) {
        PaymentCallback(successUrl, cancelUrl, failureUrl)
      } else null
    )
  }

  /**
   * 기본 Payment 도메인
   */
  fun createPayment(
    id: String = "payment_${System.currentTimeMillis()}",
    merchantId: String = "merchant_123",
    orderId: String = "order_123",
    orderName: String = "테스트 상품",
    amount: BigDecimal = BigDecimal(10000),
    currency: Currency = Currency.KRW,
    method: PaymentMethod = PaymentMethod.CARD,
    status: PaymentStatus = PaymentStatus.PENDING
  ): Payment {
    return Payment.create(
      paymentSeq = paymentSeq,
      paymentId = paymentId,
      merchantId = merchantId,
      orderId = orderId,
      orderName = orderName,
      amount = amount,
      currency = currency,
      method = method,
      methodDetail = when (method) {
        PaymentMethod.CARD -> createCardMethodDetail()
        PaymentMethod.BANK_TRANSFER -> createBankTransferMethodDetail()
        else -> createCardMethodDetail()
      }
    )
  }

  /**
   * 카드 결제 수단 상세
   */
  fun createCardMethodDetail(
    cardNumber: String = "1234567890123456",
    cardExpiry: String = "12/25",
    cardCvc: String = "123",
    installmentPlan: Int = 0,
    cardType: PaymentCardType = PaymentCardType.CREDIT,
    useCardPoint: Boolean = false
  ): PaymentMethodDetails.Card {
    return PaymentMethodDetails.Card(
      cardNumber = cardNumber,
      cardExpiry = cardExpiry,
      cardCvc = cardCvc,
      installmentPlan = installmentPlan,
      cardType = cardType,
      useCardPoint = useCardPoint
    )
  }

  /**
   * 계좌이체 결제 수단 상세
   */
  fun createBankTransferMethodDetail(
    bankCode: String = "004",
    accountNumber: String = "1234567890"
  ): PaymentMethodDetails.BankTransfer {
    return PaymentMethodDetails.BankTransfer(
      bankCode = bankCode,
      accountNumber = accountNumber
    )
  }
}

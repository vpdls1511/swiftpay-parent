package com.ngyu.swiftpay.core.common.exception

sealed class PaymentException(
  val errorCode: String,
  message: String,
  cause: Throwable? = null
) : RuntimeException(message, cause)

class OrderNotFoundException(message: String = "주문을 찾을 수 없습니다.") : PaymentException("ORDER_NOT_FOUND", message)
class PaymentNotFoundException(message: String = "결제 정보를 찾을 수 없습니다.") : PaymentException("PAYMENT_NOT_FOUND", message)
class PaymentProcessException(message: String = "결제 처리 중 문제가 발생했습니다.") : PaymentException("FAILURE_PAYMENT_PROCESS", message)
class EscrowHoldException(message: String = "에스크로 예치 중 문제가 발생했습니다.") : PaymentException("FAILURE_ESCROW_HOLD", message)

class InvalidPaymentStatusException(message: String) : PaymentException("INVALID_PAYMENT_STATUS", message)

class PaymentPersistenceException(message: String = "결제 정보를 찾을 수 없습니다.") : PaymentException("PAYMENT_PERSISTENCE_EXCEPTION", message)


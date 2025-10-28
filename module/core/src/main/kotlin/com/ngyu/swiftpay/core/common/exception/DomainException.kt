package com.ngyu.swiftpay.core.common.exception

sealed class DomainValidationException(
  val errorCode: String,
  message: String,
  cause: Throwable? = null
) : RuntimeException(message, cause)


/**
 * Status Exception
 */
class InvalidMerchantStatusException(message: String) : DomainValidationException("INVALID_MERCHANT_STATUS", message)
class InvalidOrderStatusException(message: String) : DomainValidationException("INVALID_ORDER_STATUS", message)
class InvalidPaymentStatusException(message: String) : DomainValidationException("INVALID_PAYMENT_STATUS", message)
class InvalidEscrowStatusException(message: String) : DomainValidationException("INVALID_ESCROW_STATUS", message)
class InvalidSettlementStatusException(message: String) : DomainValidationException("INVALID_SETTLEMENT_STATUS", message)

/**
 * Field Exception
 */
class InvalidAmountException(message: String) : DomainValidationException("INVALID_AMOUNT", message)
class InvalidFieldException(message: String) : DomainValidationException("INVALID_Field", message)

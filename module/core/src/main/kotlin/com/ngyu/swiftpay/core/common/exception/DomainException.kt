package com.ngyu.swiftpay.core.common.exception

sealed class ValidationException(
  val errorCode: String,
  message: String,
  cause: Throwable? = null
) : RuntimeException(message, cause)


/**
 * Status Exception
 */
class InvalidMerchantStatusException(message: String) : ValidationException("INVALID_MERCHANT_STATUS", message)
class InvalidOrderStatusException(message: String) : ValidationException("INVALID_ORDER_STATUS", message)
class InvalidPaymentStatusException(message: String) : ValidationException("INVALID_PAYMENT_STATUS", message)
class InvalidBankStatusException(message: String) : ValidationException("INVALID_BANK_STATUS", message)
class InvalidEscrowStatusException(message: String) : ValidationException("INVALID_ESCROW_STATUS", message)
class InvalidSettlementStatusException(message: String) : ValidationException("INVALID_SETTLEMENT_STATUS", message)

/**
 * Field Exception
 */
class InvalidAmountException(message: String) : ValidationException("INVALID_AMOUNT", message)
class InvalidPasswordException(message: String) : ValidationException("INVALID_PASSWORD", message)

/**
 * ETC... (추후 분류해야할것들)
 */
  class InvalidBankAccountNumberException(message: String) : ValidationException("INVALID_BANK_ACCOUNT_NUMBER", message)

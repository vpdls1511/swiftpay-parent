package com.ngyu.swiftpay.core.port

interface SequenceGenerator {
  fun nextBankAccountNumber(): Long
  fun nextMerchantId(): Long
  fun nextPaymentId(): Long
  fun nextOrderId(): Long
  fun nextEscrowId(): Long
  fun nextSettlementId(): Long
}

package com.ngyu.swiftpay.core.port

import com.ngyu.swiftpay.core.domain.escrow.Escrow

interface EscrowRepository {
  fun save(domain: Escrow): Escrow
  fun findByEscrow(domain: Escrow): Escrow

  fun findBySettlementId(settlementId: String): List<Escrow>
  fun findByPaymentId(paymentId: String): Escrow?
}

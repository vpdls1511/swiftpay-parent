package com.ngyu.swiftpay.infrastructure.db.sequence

import com.ngyu.swiftpay.core.port.SequenceGenerator
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component

@Component
class SequenceGeneratorImpl(
  private val entityManager: EntityManager,
) : SequenceGenerator {

  override fun nextBankAccountNumber(): Long = nextVal("account_number_seq")
  override fun nextMerchantId(): Long = nextVal("merchant_number_seq")
  override fun nextPaymentId(): Long = nextVal("payment_number_seq")
  override fun nextOrderId(): Long = nextVal("order_number_seq")
  override fun nextEscrowId(): Long = nextVal("escrow_number_seq")
  override fun nextSettlementId(): Long = nextVal("settlement_number_seq")

  private fun nextVal(sequenceName: String): Long {
    val query = entityManager.createNativeQuery(
      "SELECT NEXT VALUE FOR $sequenceName"
    )
    return (query.singleResult as Number).toLong()
  }
}

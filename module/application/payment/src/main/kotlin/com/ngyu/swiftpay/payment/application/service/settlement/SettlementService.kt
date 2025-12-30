package com.ngyu.swiftpay.payment.application.service.settlement

import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.domain.escrow.Escrow
import com.ngyu.swiftpay.core.domain.settlement.Settlement
import com.ngyu.swiftpay.core.port.generator.SequenceGenerator
import com.ngyu.swiftpay.core.port.repository.SettlementRepository
import org.springframework.stereotype.Service

@Service
class SettlementService(
  private val sequenceGenerator: SequenceGenerator,
  private val settlementRepository: SettlementRepository
) {
  private val log = logger()

  /**
   * 정산을 위한 Settlement 저장
   */
  fun pending(escrow: Escrow, merchantId: String): Settlement {
    val settlementSeq = sequenceGenerator.nextSettlementId()
    val settlementId = Settlement.createSettlementId(settlementSeq)
    escrow.settle()

    val settlement = Settlement.create(
      seq = settlementSeq,
      settlementId = settlementId,
      escrowId = escrow.escrowId,
      merchantId = merchantId,
      totalAmount = escrow.amount,
      fee = escrow.amount / 10 // 수수료 10%
    )

    return settlementRepository.save(settlement)
  }
}

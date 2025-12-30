package com.ngyu.swiftpay.payment.application.service.escrow

import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.domain.escrow.Escrow
import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.settlement.Settlement
import com.ngyu.swiftpay.core.port.generator.SequenceGenerator
import com.ngyu.swiftpay.core.port.repository.EscrowRepository
import com.ngyu.swiftpay.payment.application.service.settlement.SettlementService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class EscrowService(
  private val escrowRepository: EscrowRepository,
  private val settlementService: SettlementService,
  private val sequenceGenerator: SequenceGenerator
) {

  private val log = logger()

  @Transactional
  fun hold(payment: Payment): Escrow {
    log.info("에스크로 예치 시작 | paymentId=${payment.paymentId}, amount=${payment.amount}")
    val escrowId = sequenceGenerator.nextEscrowId()
    val escrow = Escrow.hold(escrowId, payment)
    val savedEscrow = escrowRepository.save(escrow)
    log.info("에스크로 예치 완료 | escrowId=${savedEscrow.escrowId}")
    return savedEscrow
  }

  fun settle(paymentId: Long): Settlement {
    val escrow = escrowRepository.findByPaymentId(paymentId)
    val settlement = settlementService.pending(escrow, escrow.id)

    val settleEscrow = escrow.settle()
    escrowRepository.save(settleEscrow)

    return settlement
  }

  fun refund(escrow: Escrow): Escrow {
    TODO("환불 로직 만들어야함")
  }
}

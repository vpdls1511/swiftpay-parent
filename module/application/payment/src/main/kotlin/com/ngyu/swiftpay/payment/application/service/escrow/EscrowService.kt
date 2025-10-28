package com.ngyu.swiftpay.payment.application.service.escrow

import com.ngyu.swiftpay.core.domain.escrow.Escrow
import com.ngyu.swiftpay.core.port.EscrowRepository
import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.common.logger.logger
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class EscrowService(
  private val escrowRepository: EscrowRepository
) {

  private val log = logger()

  @Transactional
  fun hold(payment: Payment): Escrow {
    log.info("에스크로 예치 시작 | paymentId=${payment.paymentId}, amount=${payment.amount}")
    val escrow = Escrow.hold(payment)
    val savedEscrow = escrowRepository.save(escrow)
    log.info("에스크로 예치 완료 | escrowId=${savedEscrow.escrowId}")
    return savedEscrow
  }

  fun settle(escrow: Escrow): Escrow {
    TODO("예치 로직 만들어야함")
  }

  fun refund(escrow: Escrow): Escrow {
    TODO("환불 로직 만들어야함")
  }
}

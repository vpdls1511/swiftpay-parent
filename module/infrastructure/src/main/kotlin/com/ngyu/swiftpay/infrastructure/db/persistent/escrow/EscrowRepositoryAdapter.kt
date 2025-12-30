package com.ngyu.swiftpay.infrastructure.db.persistent.escrow

import com.ngyu.swiftpay.core.domain.escrow.Escrow
import com.ngyu.swiftpay.core.port.repository.EscrowRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class EscrowRepositoryAdapter(
  private val repository: EscrowJpaRepository
) : EscrowRepository {
  override fun save(domain: Escrow): Escrow {
    val entity = EscrowMapper.toEntity(domain)
    val savedEntity = repository.save(entity)

    return EscrowMapper.toDomain(savedEntity)
  }

  override fun findByEscrow(domain: Escrow): Escrow {
    val escrowId = domain.id
    requireNotNull(escrowId) { "Settlement id가 Null 입니다" }

    val entity = repository.findByIdOrNull(escrowId)
      ?: throw Exception("결제를 찾을 수 없습니다: $escrowId")

    return EscrowMapper.toDomain(entity)
  }

  override fun findBySettlementId(settlementId: String): List<Escrow> {
    TODO("Not yet implemented")
  }

  override fun findByPaymentId(paymentId: String): Escrow {
    val entity = repository.findByPaymentId(paymentId)
      ?: throw Exception("결제를 찾을 수 없습니다: $paymentId")

    return EscrowMapper.toDomain(entity)
  }
}

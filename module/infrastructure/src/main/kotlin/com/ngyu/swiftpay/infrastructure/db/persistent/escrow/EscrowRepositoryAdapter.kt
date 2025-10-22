package com.ngyu.swiftpay.infrastructure.db.persistent.escrow

import com.ngyu.swiftpay.core.domain.escrow.Escrow
import com.ngyu.swiftpay.core.domain.escrow.EscrowRepository
import org.springframework.stereotype.Component

@Component
class EscrowRepositoryAdapter(
  private val repository: EscrowJpaRepository
): EscrowRepository {
  override fun save(domain: Escrow): Escrow {
    TODO("Not yet implemented")
  }

  override fun findByEscrow(domain: Escrow): Escrow {
    TODO("Not yet implemented")
  }
}

package com.ngyu.swiftpay.infrastructure.db.persistent.settlement

import com.ngyu.swiftpay.core.domain.settlement.Settlement
import com.ngyu.swiftpay.core.domain.settlement.SettlementRepository
import org.springframework.stereotype.Component

@Component
class SettlementRepositoryAdapter(
  private val repository: SettlementJpaRepository
): SettlementRepository {
  override fun save(domain: Settlement): Settlement {
    TODO("Not yet implemented")
  }

  override fun findBySettlement(domain: Settlement): Settlement {
    TODO("Not yet implemented")
  }
}

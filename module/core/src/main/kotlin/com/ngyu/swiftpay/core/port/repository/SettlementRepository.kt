package com.ngyu.swiftpay.core.port.repository

import com.ngyu.swiftpay.core.domain.settlement.Settlement

interface SettlementRepository {
  fun save(domain: Settlement): Settlement
  fun findBySettlement(domain: Settlement): Settlement

  fun findByPendingSettlement(): List<Settlement>
}

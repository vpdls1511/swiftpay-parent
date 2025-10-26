package com.ngyu.swiftpay.core.port

import com.ngyu.swiftpay.core.domain.settlement.Settlement

interface SettlementRepository {
  fun save(domain: Settlement): Settlement
  fun findBySettlement(domain: Settlement): Settlement
}

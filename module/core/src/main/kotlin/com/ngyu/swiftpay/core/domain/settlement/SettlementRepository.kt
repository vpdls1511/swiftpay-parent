package com.ngyu.swiftpay.core.domain.settlement

interface SettlementRepository {
  fun save(domain: Settlement): Settlement
  fun findBySettlement(domain: Settlement): Settlement
}

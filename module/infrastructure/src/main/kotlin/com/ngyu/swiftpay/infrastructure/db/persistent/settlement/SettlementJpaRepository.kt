package com.ngyu.swiftpay.infrastructure.db.persistent.settlement

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SettlementJpaRepository: JpaRepository<SettlementEntity, Long> {
}

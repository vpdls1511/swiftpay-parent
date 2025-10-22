package com.ngyu.swiftpay.infrastructure.db.persistent.escrow

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EscrowJpaRepository: JpaRepository<EscrowEntity, Long> {
}

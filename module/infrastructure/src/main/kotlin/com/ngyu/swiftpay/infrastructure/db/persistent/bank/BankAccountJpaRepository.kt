package com.ngyu.swiftpay.infrastructure.db.persistent.bank

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BankAccountJpaRepository: JpaRepository<BankAccountEntity, Long> {
  fun existsByAccountNumber(accountNumber: String): Boolean
}

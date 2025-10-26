package com.ngyu.swiftpay.infrastructure.db.sequence

import com.ngyu.swiftpay.core.port.SequenceGenerator
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component

@Component
class DatabaseSequenceGenerator(
  private val entityManager: EntityManager,
): SequenceGenerator {
  override fun nextVal(): Long {
    val query = entityManager.createNativeQuery(
      "SELECT nextval(account_number_seq)"
    )

    return (query.singleResult as Number).toLong()
  }
}

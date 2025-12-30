package com.ngyu.swiftpay.infrastructure.db.persistent.settlement

import com.ngyu.swiftpay.core.domain.settlement.Settlement
import com.ngyu.swiftpay.core.domain.settlement.SettlementStatus
import com.ngyu.swiftpay.core.port.repository.SettlementRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class SettlementRepositoryAdapter(
  private val repository: SettlementJpaRepository,
  private val jpaQueryFactory: JPAQueryFactory
): SettlementRepository {

  private val settlement = QSettlementEntity.settlementEntity

  override fun save(domain: Settlement): Settlement {
    val entity = SettlementMapper.toEntity(domain)
    val savedEntity = repository.save(entity)

    return SettlementMapper.toDomain(savedEntity)
  }

  override fun findBySettlement(domain: Settlement): Settlement {
    val settlementId = domain.id
    requireNotNull(settlementId) { "Settlement id가 Null 입니다" }

    val entity = repository.findByIdOrNull(settlementId)
      ?: throw Exception("결제를 찾을 수 없습니다: $settlementId")

    return SettlementMapper.toDomain(entity)
  }

  override fun findByPendingSettlement(): List<Settlement> {
    val entities = jpaQueryFactory
      .selectFrom(settlement)
      .where(settlement.status.eq(SettlementStatus.PENDING))
      .fetch()

    return entities.map { SettlementMapper.toDomain(it) }
  }
}

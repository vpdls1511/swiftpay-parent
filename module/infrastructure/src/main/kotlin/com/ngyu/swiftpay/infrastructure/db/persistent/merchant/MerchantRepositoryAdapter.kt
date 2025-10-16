package com.ngyu.swiftpay.infrastructure.db.persistent.merchant

import com.ngyu.swiftpay.core.domain.merchant.Merchant
import com.ngyu.swiftpay.core.domain.merchant.MerchantRepository
import org.springframework.stereotype.Component

@Component
class MerchantRepositoryAdapter(
  private val merchantRepository: MerchantJpaRepository
): MerchantRepository {
  override fun save(domain: Merchant): Merchant {
    val entity = MerchantMapper.toEntity(domain)
    val saveEntity = merchantRepository.save(entity)

    return MerchantMapper.toDomain(saveEntity)
  }
}

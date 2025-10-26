package com.ngyu.swiftpay.infrastructure.db.persistent.merchant

import com.ngyu.swiftpay.core.domain.merchant.Merchant
import com.ngyu.swiftpay.core.port.MerchantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class MerchantRepositoryAdapter(
  private val merchantRepository: MerchantJpaRepository
): MerchantRepository {
  override fun save(domain: Merchant): Merchant {
    val entity = MerchantMapper.toEntity(domain)
    val saveEntity = merchantRepository.saveAndFlush(entity)

    return MerchantMapper.toDomain(saveEntity)
  }

  override fun findByMerchantId(merchantId: String): Merchant {
    val entity = merchantRepository.findByIdOrNull(merchantId)
      ?: throw Exception("Merchant not found")

    return MerchantMapper.toDomain(entity)
  }
}

package com.ngyu.swiftpay.infrastructure.db.persistent.merchant

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MerchantJpaRepository : JpaRepository<MerchantEntity, Long> {
  fun findByMerchantId(merchantId: String): MerchantEntity?
}

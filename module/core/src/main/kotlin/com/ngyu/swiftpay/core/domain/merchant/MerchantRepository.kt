package com.ngyu.swiftpay.core.domain.merchant

interface MerchantRepository {
  fun save(domain: Merchant): Merchant
  fun findByMerchantId(merchantId: String): Merchant
}

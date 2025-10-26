package com.ngyu.swiftpay.core.port

import com.ngyu.swiftpay.core.domain.merchant.Merchant

interface MerchantRepository {
  fun save(domain: Merchant): Merchant
  fun findByMerchantId(merchantId: String): Merchant
}

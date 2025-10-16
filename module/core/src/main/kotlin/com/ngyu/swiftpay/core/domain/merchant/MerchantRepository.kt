package com.ngyu.swiftpay.core.domain.merchant

interface MerchantRepository {
  fun save(domain: Merchant): Merchant
}

package com.ngyu.swiftpay.infrastructure.db.persistent.apiKey

import com.ngyu.swiftpay.core.domain.apiKey.ApiKey

interface ApiKeyRepository{
  fun save(domain: ApiKey): ApiKey
}

package com.ngyu.swiftpay.core.domain.apiKey

interface ApiKeyRepository{
  fun save(domain: ApiKey): ApiKey
  fun findApiKey(lookupKey: String): ApiKey
}

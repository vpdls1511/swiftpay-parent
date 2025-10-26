package com.ngyu.swiftpay.core.port

import com.ngyu.swiftpay.core.domain.apiCredentials.ApiCredentials

interface ApiCredentialsRepository{
  fun save(domain: ApiCredentials): ApiCredentials
  fun findApiKey(lookupKey: String): ApiCredentials
}

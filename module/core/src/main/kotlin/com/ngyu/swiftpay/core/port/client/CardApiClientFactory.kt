package com.ngyu.swiftpay.core.port.client

import com.ngyu.swiftpay.core.domain.payment.Payment

interface CardApiClientFactory {
  fun getClient(domain: Payment): CardApiClientPort
}

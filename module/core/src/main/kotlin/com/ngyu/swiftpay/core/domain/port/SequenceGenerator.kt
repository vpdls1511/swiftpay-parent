package com.ngyu.swiftpay.core.domain.port

interface SequenceGenerator {
  fun nextVal(): Long
}

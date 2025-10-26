package com.ngyu.swiftpay.core.port

interface SequenceGenerator {
  fun nextVal(): Long
}

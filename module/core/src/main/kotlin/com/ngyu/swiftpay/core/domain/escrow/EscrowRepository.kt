package com.ngyu.swiftpay.core.domain.escrow

interface EscrowRepository {
  fun save(domain: Escrow): Escrow
  fun findByEscrow(domain: Escrow): Escrow
}

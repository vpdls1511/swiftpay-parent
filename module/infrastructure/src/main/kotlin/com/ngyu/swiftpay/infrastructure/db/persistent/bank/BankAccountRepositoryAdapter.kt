package com.ngyu.swiftpay.infrastructure.db.persistent.bank

import com.ngyu.swiftpay.core.domain.bank.BankAccount
import com.ngyu.swiftpay.core.domain.bank.BankAccountRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class BankAccountRepositoryAdapter(
  private val repository: BankAccountJpaRepository
): BankAccountRepository {
  override fun save(domain: BankAccount): BankAccount {
    val entity = BankAccountMapper.toEntity(domain)
    val savedEntity = repository.save(entity)

    return BankAccountMapper.toDomain(savedEntity)
  }

  override fun findByBankAccount(domain: BankAccount): BankAccount {
    val bankAccountId = domain.id
    requireNotNull(bankAccountId) { "BankAccountId가 없습니다." }

    val entity = repository.findByIdOrNull(bankAccountId)
      ?: throw EntityNotFoundException("계좌번호를 찾을 수 없습니다.")

    return BankAccountMapper.toDomain(entity)
  }

  override fun existByBankAccount(accountNumber: String): Boolean {
    return repository.existsByAccountNumber(accountNumber)
  }
}

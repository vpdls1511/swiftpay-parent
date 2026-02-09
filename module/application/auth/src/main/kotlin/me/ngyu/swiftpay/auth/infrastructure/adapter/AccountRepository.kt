package me.ngyu.swiftpay.auth.infrastructure.adapter

import com.ngyu.swiftpay.core.exception.SwiftError
import com.ngyu.swiftpay.core.exception.SwiftException
import me.ngyu.swiftpay.auth.domain.account.Account
import me.ngyu.swiftpay.auth.domain.account.AccountCredentials
import me.ngyu.swiftpay.auth.domain.account.AccountProfile
import me.ngyu.swiftpay.auth.domain.account.port.AccountRepositoryPort
import me.ngyu.swiftpay.auth.infrastructure.persistent.AccountCredentialsJpaRepository
import me.ngyu.swiftpay.auth.infrastructure.persistent.AccountJpaRepository
import me.ngyu.swiftpay.auth.infrastructure.persistent.AccountProfileJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class AccountRepositoryAdapter(
  private val accountJpaRepository: AccountJpaRepository,
  private val credentialsJpaRepository: AccountCredentialsJpaRepository,
  private val profileJpaRepository: AccountProfileJpaRepository
) : AccountRepositoryPort {

  // Account
  @Transactional
  override fun save(account: Account): Account {
    return accountJpaRepository.save(account)
  }

  override fun findByUsername(username: String): AccountCredentials {
    return credentialsJpaRepository.findByUsername(username)
      ?: throw SwiftException(SwiftError.RESOURCE_NOT_FOUND, "사용자를 찾을 수 없습니다: $username")
  }

  override fun findById(accountId: Long): Account {
    return accountJpaRepository.findByIdOrNull(accountId)
      ?: throw SwiftException(SwiftError.RESOURCE_NOT_FOUND, "계정을 찾을 수 없습니다: $accountId")
  }

  // Credentials
  @Transactional
  override fun saveCredentials(credentials: AccountCredentials): AccountCredentials {
    return credentialsJpaRepository.save(credentials)
  }

  override fun findByUuid(uuid: String): Account {
    return accountJpaRepository.findByUuid(uuid)
      ?: throw SwiftException(SwiftError.RESOURCE_NOT_FOUND, "사용자를 찾을 수 없습니다: $uuid")
  }

  override fun existsByUsername(username: String): Boolean {
    return credentialsJpaRepository.existsByUsername(username)
  }

  // Profile
  @Transactional
  override fun saveProfile(profile: AccountProfile): AccountProfile {
    return profileJpaRepository.save(profile)
  }

  override fun existsByEmail(email: String): Boolean {
    return profileJpaRepository.existsByEmail(email)
  }

}

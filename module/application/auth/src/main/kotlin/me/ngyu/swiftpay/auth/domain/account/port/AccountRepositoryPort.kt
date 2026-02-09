package me.ngyu.swiftpay.auth.domain.account.port

import me.ngyu.swiftpay.auth.domain.account.Account
import me.ngyu.swiftpay.auth.domain.account.AccountCredentials
import me.ngyu.swiftpay.auth.domain.account.AccountProfile

interface AccountRepositoryPort {
  // Account
  fun save(account: Account): Account
  fun findById(accountId: Long): Account
  fun findByUuid(uuid: String): Account

  // Credentials
  fun saveCredentials(credentials: AccountCredentials): AccountCredentials
  fun findByUsername(username: String): AccountCredentials
  fun existsByUsername(username: String): Boolean

  // Profile
  fun saveProfile(profile: AccountProfile): AccountProfile
  fun existsByEmail(email: String): Boolean
}

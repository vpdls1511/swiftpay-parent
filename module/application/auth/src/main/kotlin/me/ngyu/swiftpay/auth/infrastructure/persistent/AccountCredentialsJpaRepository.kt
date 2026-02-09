package me.ngyu.swiftpay.auth.infrastructure.persistent

import me.ngyu.swiftpay.auth.domain.account.AccountCredentials
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountCredentialsJpaRepository: JpaRepository<AccountCredentials, Long> {
  fun findByUsername(username: String): AccountCredentials?
  fun existsByUsername(username: String): Boolean
}

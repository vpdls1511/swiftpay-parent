package me.ngyu.swiftpay.auth.infrastructure.persistent

import me.ngyu.swiftpay.auth.domain.account.AccountProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountProfileJpaRepository: JpaRepository<AccountProfile, Long> {
  fun existsByEmail(email: String): Boolean
}

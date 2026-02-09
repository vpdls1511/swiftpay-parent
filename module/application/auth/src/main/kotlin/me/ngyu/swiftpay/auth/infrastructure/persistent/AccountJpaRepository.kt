package me.ngyu.swiftpay.auth.infrastructure.persistent

import me.ngyu.swiftpay.auth.domain.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountJpaRepository: JpaRepository<Account, Long> {
  fun findByUuid(uuid: String): Account?
}

package me.ngyu.swiftpay.auth.application.service

import com.ngyu.swiftpay.core.exception.SwiftError
import com.ngyu.swiftpay.core.exception.SwiftException
import me.ngyu.swiftpay.auth.application.api.dto.AccountRequest
import me.ngyu.swiftpay.auth.application.usecase.AccountCredentialsUseCase
import me.ngyu.swiftpay.auth.application.usecase.AccountProfileUseCase
import me.ngyu.swiftpay.auth.application.usecase.AccountUseCase
import me.ngyu.swiftpay.auth.domain.account.Account
import me.ngyu.swiftpay.auth.domain.account.AccountCredentials
import me.ngyu.swiftpay.auth.domain.account.AccountProfile
import me.ngyu.swiftpay.auth.domain.account.port.AccountRepositoryPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
  private val accountRepositoryPort: AccountRepositoryPort,
  private val passwordEncoder: PasswordEncoder,
) : AccountUseCase, AccountCredentialsUseCase, AccountProfileUseCase {

  @Transactional
  override fun saveUser(request: AccountRequest): String {
    this.accountValidation(request)

    val encodedPassword = passwordEncoder.encode(request.password)

    val account = Account()
    val savedAccount = accountRepositoryPort.save(account)

    accountRepositoryPort.saveCredentials(
      AccountCredentials(
        account = savedAccount,
        username = request.username,
        password = encodedPassword
      )
    )
    accountRepositoryPort.saveProfile(
      AccountProfile(
        account = savedAccount,
        name = request.name,
        email = request.email,
        phone = request.phone,
      )
    )

    return savedAccount.uuid
  }

  private fun accountValidation(request: AccountRequest) {
    if (accountRepositoryPort.existsByUsername(request.username)) {
      throw SwiftException(SwiftError.CONFLICT, "이미 존재하는 계정정보 입니다.")
    }

    if (accountRepositoryPort.existsByEmail(request.email)) {
      throw SwiftException(SwiftError.CONFLICT, "이미 존재하는 이메일 입니다.")
    }
  }

}

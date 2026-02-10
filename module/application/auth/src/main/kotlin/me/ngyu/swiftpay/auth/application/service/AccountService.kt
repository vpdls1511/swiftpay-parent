package me.ngyu.swiftpay.auth.application.service

import com.ngyu.swiftpay.core.exception.SwiftError
import com.ngyu.swiftpay.core.exception.SwiftException
import me.ngyu.swiftpay.auth.api.dto.AccountRequest
import me.ngyu.swiftpay.auth.api.dto.LoginRequest
import me.ngyu.swiftpay.auth.api.dto.TokenDto
import me.ngyu.swiftpay.auth.application.usecase.AccountUseCase
import me.ngyu.swiftpay.auth.domain.account.Account
import me.ngyu.swiftpay.auth.domain.account.AccountCredentials
import me.ngyu.swiftpay.auth.domain.account.AccountProfile
import me.ngyu.swiftpay.auth.domain.account.port.AccountRepositoryPort
import me.ngyu.swiftpay.common.security.jwt.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
  private val accountRepositoryPort: AccountRepositoryPort,
  private val passwordEncoder: PasswordEncoder,
  private val tokenProvider: JwtTokenProvider
) : AccountUseCase {

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

  override fun login(request: LoginRequest): TokenDto {
    val user = accountRepositoryPort.findByUsername(request.username)
    if(!passwordEncoder.matches(request.password, user.password)) {
      throw SwiftException(SwiftError.UNAUTHORIZED, "비밀번호가 일치하지 않습니다")
    }

    return TokenDto(
      accessToken = tokenProvider.createAccessToken(user.account.uuid, user.account.id, user.account.role.name),
      refreshToken = tokenProvider.createRefreshToken(user.account.uuid),
    )
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

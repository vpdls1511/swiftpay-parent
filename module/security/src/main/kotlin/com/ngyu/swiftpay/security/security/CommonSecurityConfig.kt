package me.ngyu.swiftpay.common.security

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.stereotype.Component

@Component
class CommonSecurityConfig: CommonSecurity {

  override fun security(http: HttpSecurity) {
    http
      .csrf { it.disable() }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
  }

}

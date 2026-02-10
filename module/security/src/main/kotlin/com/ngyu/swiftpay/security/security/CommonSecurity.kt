package me.ngyu.swiftpay.common.security

import org.springframework.security.config.annotation.web.builders.HttpSecurity

interface CommonSecurity {
  fun security(http: HttpSecurity)
}

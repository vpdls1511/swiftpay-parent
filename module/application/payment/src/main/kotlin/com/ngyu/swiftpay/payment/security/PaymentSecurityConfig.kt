package com.ngyu.swiftpay.payment.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class PaymentSecurityConfig(
  private val paymentTokenValidator: PaymentTokenValidator
) {

  @Bean
  fun paymentSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
    return http
      .securityMatcher("/**")
      .addFilterBefore(
        PaymentTokenFilter(paymentTokenValidator),
        UsernamePasswordAuthenticationFilter::class.java
      )
      .csrf { it.disable() }
      .build()

  }

}

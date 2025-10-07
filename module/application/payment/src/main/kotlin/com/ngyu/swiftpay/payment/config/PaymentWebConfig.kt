package com.ngyu.swiftpay.payment.config

import com.ngyu.swiftpay.payment.security.PaymentPrincipalResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class PaymentWebConfig (
  private val paymentPrincipalResolver: PaymentPrincipalResolver
): WebMvcConfigurer {
  override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
    resolvers.add(paymentPrincipalResolver)
  }
}

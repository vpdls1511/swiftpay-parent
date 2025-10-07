package com.ngyu.swiftpay.payment.security

import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class PaymentPrincipalResolver: HandlerMethodArgumentResolver {

  override fun supportsParameter(parameter: MethodParameter): Boolean {
    return parameter.hasParameterAnnotation(PaymentPrincipal::class.java) &&
        parameter.parameterType == PaymentCredentials::class.java
  }

  override fun resolveArgument(
    parameter: MethodParameter,
    mavContainer: ModelAndViewContainer?,
    webRequest: NativeWebRequest,
    binderFactory: WebDataBinderFactory?
  ): PaymentCredentials {
    val authentication = SecurityContextHolder.getContext().authentication
      ?: throw IllegalStateException("인증 정보가 존재하지 않습니다.")

    log.info(authentication.toString())

    val credentials = authentication.principal as? PaymentCredentials
      ?: throw IllegalStateException("유효하지 않은 인증 정보입니다.")

    return credentials
  }

}

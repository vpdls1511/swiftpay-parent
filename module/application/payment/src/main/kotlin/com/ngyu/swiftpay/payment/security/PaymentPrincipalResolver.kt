package com.ngyu.swiftpay.payment.security

import com.ngyu.swiftpay.core.exception.PrincipalException
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
class PaymentPrincipalResolver : HandlerMethodArgumentResolver {

  private val log = logger()

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

    checkNotNull(authentication) {
      log.error("filter 통과했지만, Security Context에 인증정보 없음")
      throw PrincipalException("인증 정보가 존재하지 않습니다.")
    }

    require(authentication.principal is PaymentCredentials) {
      log.error("인증 정보 타입이 PaymentPrincipal이 아님")
      throw PrincipalException("유효하지 않은 인증 정보입니다.")
    }

    return authentication.principal as PaymentCredentials
  }

}

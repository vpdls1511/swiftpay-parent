package com.ngyu.swiftpay.payment.security

import com.ngyu.swiftpay.payment.security.vo.PaymentCredentialsVo
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
        parameter.parameterType == PaymentCredentialsVo::class.java
  }

  override fun resolveArgument(
    parameter: MethodParameter,
    mavContainer: ModelAndViewContainer?,
    webRequest: NativeWebRequest,
    binderFactory: WebDataBinderFactory?
  ): PaymentCredentialsVo {
    val authentication = SecurityContextHolder.getContext().authentication
      ?: throw IllegalStateException("인증 정보가 존재하지 않습니다.")

    val credentials = authentication.principal as? PaymentCredentialsVo
      ?: throw IllegalStateException("유효하지 않은 인증 정보입니다.")

    return credentials
  }

}

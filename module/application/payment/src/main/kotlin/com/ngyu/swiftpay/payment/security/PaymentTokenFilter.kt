package com.ngyu.swiftpay.payment.security

import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials
import com.ngyu.swiftpay.security.security.BaseAuthenticationFilter
import jakarta.servlet.http.HttpServletRequest

class PaymentTokenFilter(
  private val paymentTokenValidator: PaymentTokenValidator,
) : BaseAuthenticationFilter<PaymentCredentials>() {

  companion object {
    private const val PAYMENT_API_KEY_HEADER = "X-API-KEY"
    private const val PAYMENT_API_PAIR_HEADER = "X-API-PAIR"
  }

  /**
   * Http 요청에서 인증 정보 추출
   * @return 추출된 인증 정보, 없으면 null
   */
  override fun extractCredentials(request: HttpServletRequest): PaymentCredentials? {
    val apiKey = request.getHeader(PAYMENT_API_KEY_HEADER)
    val apiPairKey = request.getHeader(PAYMENT_API_PAIR_HEADER)

    if (apiKey.isNullOrEmpty() || apiPairKey.isNullOrEmpty()) {
      return null
    }

    return PaymentCredentials(apiKey, apiPairKey)
  }

  /**
   * 인증이 필요한 url 패턴
   * @return url 패턴 리스트
   */
  override fun getProtectedPaths(): List<String> {
    return listOf(
      "/payment/**"
    )
  }

  override fun getNoProtectedPaths(): List<String> {
    return listOf(
      "/payment/api-keys/issued"
    )
  }
  /**
   * 추출된 인증정보 검증
   * @param credential 검증할 데이터
   * @return 유효하면 true, 그렇지 않으면 false
   */
  override fun validateCredentials(credential: PaymentCredentials): Boolean {
    return paymentTokenValidator.validate(credential)
  }
}


package com.ngyu.swiftpay.security.security.filter

import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.security.security.validator.ApiKeyValidator
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiKeyAuthenticationFilter (
  private val apiKeyValidator: ApiKeyValidator,
): OncePerRequestFilter() {

  private val log = logger()

  companion object {
    private const val PAYMENT_API_KEY_HEADER = "X-API-KEY"
    private const val PAYMENT_API_PAIR_HEADER = "X-API-PAIR"
  }

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    val apiKey = request.getHeader(PAYMENT_API_KEY_HEADER)
    val apiPairKey = request.getHeader(PAYMENT_API_PAIR_HEADER)

    if (apiKey.isNullOrEmpty() && apiPairKey.isNullOrEmpty()) {
      log.error("API key is empty")
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API key is empty")
      return
    }

    val isValid = apiKeyValidator.verify(apiKey, apiPairKey)

    if (!isValid) {
      log.error("API key is invalid")
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API key is invalid")
      return
    }

    filterChain.doFilter(request, response)
  }

}

package com.ngyu.swiftpay.core.logger

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RequestIdFilter : OncePerRequestFilter() {

  companion object {
    const val REQUEST_ID_HEADER = "X-Request-ID"
    const val REQUEST_ID_MDC_KEY = "requestId"
    const val REQUEST_TIME_MDC_KEY = "requestTime"
  }

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    try {
      // 헤더에서 가져오거나 새로 생성
      val requestId = request.getHeader(REQUEST_ID_HEADER)
        ?: UUID.randomUUID().toString()
      val requestTime = System.currentTimeMillis()

      // MDC에 저장
      MDC.put(REQUEST_ID_MDC_KEY, requestId)
      MDC.put(REQUEST_TIME_MDC_KEY, requestTime.toString())

      // 응답 헤더에도 추가
      response.setHeader(REQUEST_ID_HEADER, requestId)

      filterChain.doFilter(request, response)
    } finally {
      // 요청 완료 후 MDC 정리 (메모리 누수 방지)
      MDC.clear()
    }
  }
}

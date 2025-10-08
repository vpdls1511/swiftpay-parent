package com.ngyu.swiftpay.core.exception.response

import com.ngyu.swiftpay.core.logger.RequestIdFilter
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.MDC

data class ExceptionResponse(
  val message: String,
  val requestId: String,
  val timestamp: Long,
  val requestTime: Long,
  val path: String,
) {
  companion object {
    fun create(message: String, request: HttpServletRequest): ExceptionResponse {
      val requestId = MDC.get(RequestIdFilter.REQUEST_ID_MDC_KEY)
        ?: "unknown"
      val requestTime = MDC.get(RequestIdFilter.REQUEST_TIME_MDC_KEY)?.toLongOrNull()
        ?: System.currentTimeMillis()

      return ExceptionResponse(
        message = message,
        requestId = requestId,
        timestamp = System.currentTimeMillis(),
        requestTime = requestTime,
        path = request.requestURI
      )
    }
  }
}

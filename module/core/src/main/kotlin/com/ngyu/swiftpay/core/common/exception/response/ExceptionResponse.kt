package com.ngyu.swiftpay.core.common.exception.response

import com.ngyu.swiftpay.core.common.logger.RequestIdFilter
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.MDC

/**
 * 예외 발생 시 클라이언트에 반환하는 에러 응답 형식
 *
 * @property message 에러 메시지
 * @property requestId 요청 추적 ID (X-Request-ID 헤더)
 * @property timestamp 예외 발생 시각 (Unix timestamp, milliseconds)
 * @property requestTime 요청 시작 시각 (Unix timestamp, milliseconds)
 * @property path 예외가 발생한 API 경로
 */
data class ExceptionResponse(
  val errorCode: String,
  val message: String,
  val requestId: String,
  val timestamp: Long,
  val requestTime: Long,
  val path: String,
) {
  companion object {
    fun create(errorCode: String = "UNKNOWN_EXCEPTION",
               message: String,
               request: HttpServletRequest
    ): ExceptionResponse {
      val requestId = MDC.get(RequestIdFilter.REQUEST_ID_MDC_KEY)
        ?: "unknown"
      val requestTime = MDC.get(RequestIdFilter.REQUEST_TIME_MDC_KEY)?.toLongOrNull()
        ?: System.currentTimeMillis()

      return ExceptionResponse(
        errorCode = errorCode,
        message = message,
        requestId = requestId,
        timestamp = System.currentTimeMillis(),
        requestTime = requestTime,
        path = request.requestURI
      )
    }
  }
}

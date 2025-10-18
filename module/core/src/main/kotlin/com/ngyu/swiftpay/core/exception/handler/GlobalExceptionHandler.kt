package com.ngyu.swiftpay.core.exception.handler

import com.ngyu.swiftpay.core.exception.response.ExceptionResponse
import com.ngyu.swiftpay.core.logger.logger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 기본적인 예외를 처리하는 글로벌 핸들러
 *
 * core 모듈을 의존하는 모든 프로젝트의 @RestController에서 발생하는
 * 모든 Exception을 캐치하여 응답을 반환한다.
 */
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class GlobalExceptionHandler {

  private val log = logger()

  @ExceptionHandler(Exception::class)
  fun handleGlobalException(
    e: Exception,
    request: HttpServletRequest
  ): ResponseEntity<ExceptionResponse> {

    log.error("GlobalException -", e)

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(
        ExceptionResponse.create(
          message = "예기치 못한 서버 에러 발생",
          request = request
        )
      )
  }

}

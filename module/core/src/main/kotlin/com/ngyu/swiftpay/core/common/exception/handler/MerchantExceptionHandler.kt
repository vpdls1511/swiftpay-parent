package com.ngyu.swiftpay.core.common.exception.handler

import com.ngyu.swiftpay.core.common.exception.MerchantException
import com.ngyu.swiftpay.core.common.exception.response.ExceptionResponse
import com.ngyu.swiftpay.core.common.logger.logger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 가맹점 관련한 예외를 처리하는 글로벌 핸들러
 *
 * core 모듈을 의존하는 모든 프로젝트의 @RestController에서 발생하는
 * 가맹점 관련 Exception을 캐치하여 500 InternalServerError 응답을 반환한다.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class MerchantExceptionHandler {

  private val log = logger()

  @ExceptionHandler(MerchantException::class)
  fun handleMerchantException(
    e: MerchantException,
    request: HttpServletRequest
  ): ResponseEntity<ExceptionResponse> {
    log.error("MerchantException = ${e.message}")
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        ExceptionResponse.create(
          errorCode = e.errorCode,
          message = e.message ?: "가맹 문제 발생",
          request = request
        )
      )
  }
}

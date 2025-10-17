package com.ngyu.swiftpay.core.exception.handler

import com.ngyu.swiftpay.core.exception.PrincipalException
import com.ngyu.swiftpay.core.exception.response.ExceptionResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 인증 관련 예외를 처리하는 글로벌 핸들러
 *
 * core 모듈을 의존하는 모든 프로젝트의 @RestController에서 발생하는
 * 인증 관련된 Exception을 캐치하여 401 Unauthorized 응답을 반환한다.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class PrincipalExceptionHandler {

  @ExceptionHandler(PrincipalException::class)
  fun handlePrincipalException(
    ex: Exception,
    request: HttpServletRequest
  ): ResponseEntity<ExceptionResponse> {
    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(
        ExceptionResponse.create(
          message = ex.message ?: "Principal Exception",
          request = request
        )
      )
  }

}

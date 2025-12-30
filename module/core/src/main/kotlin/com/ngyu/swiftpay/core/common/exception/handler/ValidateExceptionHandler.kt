package com.ngyu.swiftpay.core.common.exception.handler

import com.ngyu.swiftpay.core.common.exception.ValidationException
import com.ngyu.swiftpay.core.common.exception.response.ExceptionResponse
import com.ngyu.swiftpay.core.common.logger.logger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class ValidateExceptionHandler {

  private val log = logger()

  @ExceptionHandler(ValidationException::class)
  fun handleValidationException(
    e: ValidationException,
    request: HttpServletRequest
  ): ResponseEntity<ExceptionResponse> {
    log.error("ValidationException = ${e.message}")
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        ExceptionResponse.create(
          errorCode = e.errorCode,
          message = e.message ?: "정합성 체크 문제 발생",
          request = request
        )
      )

  }
}

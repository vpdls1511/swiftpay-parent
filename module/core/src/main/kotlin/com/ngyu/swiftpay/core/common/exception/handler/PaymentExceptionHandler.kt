package com.ngyu.swiftpay.core.common.exception.handler

import com.ngyu.swiftpay.core.common.exception.PaymentException
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
class PaymentExceptionHandler {

  private val log = logger()

  @ExceptionHandler(PaymentException::class)
  fun handlePaymentException(
    e: PaymentException,
    request: HttpServletRequest
    ): ResponseEntity<ExceptionResponse> {
    log.error("PaymentException = ${e.message}")

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

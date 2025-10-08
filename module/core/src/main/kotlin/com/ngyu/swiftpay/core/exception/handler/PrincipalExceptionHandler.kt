package com.ngyu.swiftpay.core.exception.handler

import com.ngyu.swiftpay.core.exception.PrincipalException
import com.ngyu.swiftpay.core.exception.response.ExceptionResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
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

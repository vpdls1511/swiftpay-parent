package com.ngyu.swiftpay.core.exception.handler

import com.ngyu.swiftpay.core.exception.response.ExceptionResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(Exception::class)
  fun handleGlobalException(
    e: Exception,
    request: HttpServletRequest
  ): ResponseEntity<ExceptionResponse> {
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

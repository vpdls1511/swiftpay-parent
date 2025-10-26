package com.ngyu.swiftpay.core.common.annotation

import com.ngyu.swiftpay.core.common.exception.response.ExceptionResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponses(
  value = [
    ApiResponse(responseCode = "401", description = "인증 실패", content = [Content(schema = Schema(implementation = ExceptionResponse::class))]),
    ApiResponse(responseCode = "400", description = "잘못된 요청", content = [Content(schema = Schema(implementation = ExceptionResponse::class))]),
    ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(implementation = ExceptionResponse::class))])
  ]
)
annotation class StandardApiResponses

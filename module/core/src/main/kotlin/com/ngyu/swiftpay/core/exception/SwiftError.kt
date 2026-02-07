package com.ngyu.swiftpay.core.exception

enum class SwiftError(
  val code: String,
  val message: String,
  val status: Int
) {
  // Auth
  UNAUTHORIZED("AUTH001", "인증이 필요합니다", 401),
  INVALID_CREDENTIALS("AUTH002", "인증 정보가 유효하지 않습니다", 401),
  ACCESS_DENIED("AUTH003", "접근 권한이 없습니다", 403),

  // Payment
  PAYMENT_FAILED("PAY001", "결제에 실패했습니다", 400),
  PAYMENT_NOT_FOUND("PAY002", "결제 정보를 찾을 수 없습니다", 404),

  // Common
  INVALID_INPUT("CMN001", "입력값이 유효하지 않습니다", 400),
  RESOURCE_NOT_FOUND("CMN002", "리소스를 찾을 수 없습니다", 404),
  INTERNAL_ERROR("CMN003", "내부 오류가 발생했습니다", 500)
}


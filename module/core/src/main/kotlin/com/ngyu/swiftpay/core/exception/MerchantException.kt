package com.ngyu.swiftpay.core.exception

/**
 * 가맹점 관련 예외
 *
 * 사용 예시:
 * ```
 * throw MerchantException("가맹점 관련 알 수 없는 오류가 발생하였습니다.")
 * throw MerchantException("가맹점 관련 알 수 없는 오류가 발생하였습니다.", cause = jwtException)
 * ```
 * @property message 예외 메시지
 * @property cause 원인 예외 (선택)
 *
 * @see com.ngyu.swiftpay.core.exception.handler.PrincipalExceptionHandler Controller 레벨 예외 처리
 */
open class MerchantException(
  message: String,
  cause: Throwable? = null
) : RuntimeException(message, cause)

class MerchantNotFoundException(message: String = "가맹점을 찾을 수 없습니다")
  : MerchantException(message)

class DuplicateMerchantException(message: String = "이미 등록된 가맹점입니다")
  : MerchantException(message)

class InvalidMerchantDataException(message: String = "가맹점 정보가 유효하지 않습니다.")
  : MerchantException(message)

class MerchantUnauthorizedException(message: String = "인증되지 않은 가맹점입니다")
  : MerchantException(message)

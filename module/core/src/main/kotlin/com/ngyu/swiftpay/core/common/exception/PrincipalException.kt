package com.ngyu.swiftpay.core.common.exception

/**
 * 인증 실패 예외
 *
 * 사용 예시:
 * ```
 * throw PrincipalException("인증 정보가 없습니다.")
 * throw PrincipalException("유효하지 않은 토큰입니다.", cause = jwtException)
 * ```
 * @property message 예외 메시지
 * @property cause 원인 예외 (선택)
 *
 * @see com.ngyu.swiftpay.core.exception.handler.PrincipalExceptionHandler Controller 레벨 예외 처리
 */
class PrincipalException(
  message: String,
  cause: Throwable? = null,
) : RuntimeException(message, cause)

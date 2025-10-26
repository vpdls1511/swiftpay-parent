package com.ngyu.swiftpay.security.security

import com.ngyu.swiftpay.core.common.exception.PrincipalException
import com.ngyu.swiftpay.core.common.logger.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter


abstract class BaseAuthenticationFilter<T> : OncePerRequestFilter() {

  private val log = logger()

  override fun shouldNotFilter(request: HttpServletRequest): Boolean {
    val requestPath = request.requestURI
    val pathMatcher = AntPathMatcher()

    val isNoProtected = getNoProtectedPaths().any { pattern ->
      pathMatcher.match(pattern, requestPath)
    }
    if (isNoProtected) {
      return true
    }

    val isProtected = getProtectedPaths().any { pattern ->
      pathMatcher.match(pattern, requestPath)
    }
    return !isProtected
  }

  /**
   * Http 요청에서 인증 정보 추출
   * @return 추출된 인증 정보, 없으면 null
   */
  abstract fun extractCredentials(request: HttpServletRequest): T?

  /**
   * 추출된 인증정보 검증
   * @param credential 검증할 데이터
   * @return 유효하면 true, 그렇지 않으면 false
   */
  abstract fun validateCredentials(credential: T): Boolean

  /**
   * 인증이 필요한 url 패턴
   * @return url 패턴 리스트
   */
  abstract fun getProtectedPaths(): List<String>

  /**
   * 인증이 필요하지 않은 Url 패턴
   * @return url 패턴 리스트
   */
  abstract fun getNoProtectedPaths(): List<String>

  /**
   * 요청 필터링 후 인증 수행
   *
   * 처리순서 :
   * 1. 인증 정보 추출
   * 2. 인증 정보 검증
   * 3. 실패 시 401, 성공 시 다음 필터
   */
  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    val credentials = extractCredentials(request) ?: throw PrincipalException("인증 정보가 없습니다.")

    if (!validateCredentials(credentials)) {
      throw PrincipalException("유효하지 않은 인증 정보 입니다.")
    }

    val authentication = UsernamePasswordAuthenticationToken(
      credentials,
      null,
      emptyList()
    )

    SecurityContextHolder.getContext().authentication = authentication

    filterChain.doFilter(request, response)
  }
}

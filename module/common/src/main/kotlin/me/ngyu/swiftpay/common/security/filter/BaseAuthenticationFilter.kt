package com.ngyu.swiftpay.common.security.filter

import com.ngyu.swiftpay.core.common.exception.SwiftError
import com.ngyu.swiftpay.core.common.exception.SwiftException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

abstract class BaseAuthenticationFilter<T> : OncePerRequestFilter() {

  private val pathMatcher = AntPathMatcher()

  override fun shouldNotFilter(request: HttpServletRequest): Boolean {
    val requestPath = request.requestURI

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

  abstract fun extractCredentials(request: HttpServletRequest): T?
  abstract fun validateCredentials(credential: T): Boolean
  abstract fun getProtectedPaths(): List<String>
  abstract fun getNoProtectedPaths(): List<String>

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    val credentials = extractCredentials(request)
      ?: throw SwiftException(SwiftError.UNAUTHORIZED)

    if (!validateCredentials(credentials)) {
      throw SwiftException(SwiftError.INVALID_CREDENTIALS)
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

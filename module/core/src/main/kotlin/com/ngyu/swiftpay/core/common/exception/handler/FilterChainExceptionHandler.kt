package com.ngyu.swiftpay.core.common.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.ngyu.swiftpay.core.common.exception.PrincipalException
import com.ngyu.swiftpay.core.common.exception.response.ExceptionResponse
import com.ngyu.swiftpay.core.common.logger.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


/**
 * FilterChain 전체를 감싸는 최상위 예외처리
 *
 * filterChain.doFilter() 내부에서 발생하는 모든 에러를 캐치하여
 * 일관된 형식의 ExceptionResposne로 반환한다.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class FilterChainExceptionHandler(
  private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

  private val log = logger()

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    try {
      filterChain.doFilter(request, response)
    } catch (e: PrincipalException) {
      log.error("인증 예외 발생 : ${e.message}")

      this.writeExceptionResponse(response, request, HttpServletResponse.SC_UNAUTHORIZED, e.message ?: "인증에 실패하였습니다.")
    } catch (e: Exception) {
      log.error("Request Filter 중 예외 발생", e)

      this.writeExceptionResponse(response, request, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류가 발생하였습니다.")
    }
  }

  private fun writeExceptionResponse(
    response: HttpServletResponse,
    request: HttpServletRequest,
    code: Int,
    message: String
  ) {

    if (response.isCommitted) {
      log.warn("응답이 이미 커밋되어 추가 에러를 작성할 수 없습니다.")
      return
    }

    response.status = code
    response.contentType = MediaType.APPLICATION_JSON_VALUE
    response.characterEncoding = "UTF-8"

    try {
      val errorResponse = ExceptionResponse.create(message, request)
      val json = objectMapper.writeValueAsString(errorResponse)

      response.writer.write(json)
      response.writer.flush()
    } catch (ex: Exception) {
      log.error(ex.message, ex)
    }
  }

}

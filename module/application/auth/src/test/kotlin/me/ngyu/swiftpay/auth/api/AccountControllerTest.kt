package me.ngyu.swiftpay.auth.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.ngyu.swiftpay.core.exception.SwiftError
import com.ngyu.swiftpay.core.exception.SwiftException
import com.ngyu.swiftpay.core.exception.handler.GlobalExceptionHandler
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import me.ngyu.swiftpay.auth.api.AccountController
import me.ngyu.swiftpay.auth.api.dto.AccountRequest
import me.ngyu.swiftpay.auth.application.usecase.AccountUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@WebMvcTest(AccountController::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler::class)
class AccountControllerTest {

  private final val TEST_ACCOUNT_REGISTER_URL = "/v1/api/account/register"

  @Autowired
  private lateinit var mockMvc: MockMvc

  @Autowired
  private lateinit var objectMapper: ObjectMapper


  @MockkBean
  private lateinit var accountUseCase: AccountUseCase

  @Test
  fun `회원가입 성공 - 201 Created`() {
    // given
    val request = AccountRequest("user", "pass", "홍길동", "test@test.com", "010-1234-5678")
    every { accountUseCase.saveUser(any()) } returns "test-uuid"

    // when & then
    mockMvc.perform(
      post(TEST_ACCOUNT_REGISTER_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
    )
      .andExpect(status().isCreated)
      .andExpect(header().string("Location", "/accounts/test-uuid"))
  }

  @Test
  fun `중복 username - 409 Conflict`() {
    every { accountUseCase.saveUser(any()) } throws SwiftException(
      SwiftError.CONFLICT
    )

    mockMvc.perform(
      post(TEST_ACCOUNT_REGISTER_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(
          """
{
        "username":"user",
        "password":"pass",
        "name":"홍길동",
        "email":"test@test.com",
        "phone":"010-1234-5678"
    }
""".trimIndent()
        )
    )
      .andExpect(status().isConflict)
  }
}

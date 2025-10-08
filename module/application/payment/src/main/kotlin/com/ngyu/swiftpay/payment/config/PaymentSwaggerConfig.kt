package com.ngyu.swiftpay.payment.config

import com.ngyu.swiftpay.core.config.BaseSwaggerConfig
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PaymentSwaggerConfig : BaseSwaggerConfig() {

  private val PAYMENT_API_TITLE = "swift-payment"

  @Bean
  fun paymentApi(): GroupedOpenApi {
    return GroupedOpenApi.builder()
      .group("payment")
      .pathsToMatch("/payment/**")
      .build()
  }

  @Bean
  fun paymentOpenApi(): OpenAPI {
    return OpenAPI()
      .info(
        createBaseInfo(
          title = PAYMENT_API_TITLE,
          description = """
            # SwiftPay 결제 API
            
            결제 처리를 위한 RESTful API입니다.
            
            ## 인증
            모든 API 요청에는 다음 헤더가 필요합니다:
            - `X-API-KEY`: API 키
            - `X-API-PAIR`: API 페어 키
            
            ## API 키 발급
            `POST /payment/api-keys/issued` 를 통해 키를 발급받을 수 있습니다.
        """.trimIndent(),
        )
      ).servers(
        listOf(
          Server().url("http://localhost:8080").description("로컬"),
          Server().url("https://swiftpay-payment.ngyu.me").url("개발")
        )
      ).addSecurityItem(
        SecurityRequirement()
          .addList("Api Key")
      ).components(
        Components()
          .addSecuritySchemes(
            "X-API-KEY",
            SecurityScheme()
              .type(SecurityScheme.Type.APIKEY)
              .`in`(SecurityScheme.In.HEADER)
              .name("X-API-KEY")
              .description("결제 API 인증 키")
          )
          .addSecuritySchemes(
            "X-API-PAIR",
            SecurityScheme()
              .type(SecurityScheme.Type.APIKEY)
              .`in`(SecurityScheme.In.HEADER)
              .name("X-API-PAIR")
              .description("결제 API 페어 키")
          )
      )
  }

}

package com.ngyu.swiftpay.core.config

import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info

abstract class BaseSwaggerConfig {

  protected fun createContact(): Contact {
    return Contact()
      .name("SwiftPay")
//      .email("support@swiftpay.com")
//      .url("https://swiftpay.com")
  }

  protected fun createBaseInfo(
    title: String,
    description: String,
    version: String? = "1.0.0",
  ): Info {
    return Info()
      .title(title)
      .description(description)
      .version(version)
      .contact(createContact())
  }

}

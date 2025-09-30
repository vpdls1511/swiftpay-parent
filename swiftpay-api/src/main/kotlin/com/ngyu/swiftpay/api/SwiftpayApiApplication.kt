package com.ngyu.swiftpay.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
  scanBasePackages = ["com.ngyu.swiftpay"],
  exclude = [SecurityAutoConfiguration::class]  // Security 자동 설정 제외
)
class SwiftpayApiApplication

fun main(args: Array<String>) {
  runApplication<SwiftpayApiApplication>(*args)
}

package com.ngyu.swiftpay.payment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.ngyu.swiftpay"])
class SwiftpayApiApplication

fun main(args: Array<String>) {
  runApplication<SwiftpayApiApplication>(*args)
}

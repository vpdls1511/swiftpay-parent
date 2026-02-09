package me.ngyu.swiftpay.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
  scanBasePackages = ["me.ngyu.swiftpay"]
)
class AuthApplication

fun main(args: Array<String>) {
  runApplication<AuthApplication>(*args)
}

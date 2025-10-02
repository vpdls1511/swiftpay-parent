package com.ngyu.swiftpay.payment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan(basePackages = ["com.ngyu.swiftpay.infrastructure.db.persistent"])
@EnableJpaRepositories(basePackages = ["com.ngyu.swiftpay.infrastructure.db.persistent"])
@SpringBootApplication(scanBasePackages = ["com.ngyu.swiftpay"])
class SwiftpayApiApplication

fun main(args: Array<String>) {
  runApplication<SwiftpayApiApplication>(*args)
}

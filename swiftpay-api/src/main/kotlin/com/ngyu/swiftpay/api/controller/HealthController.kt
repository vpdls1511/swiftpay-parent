package com.ngyu.swiftpay.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/health"], produces = ["application/json;charset=UTF-8"])
class HealthController {

  @GetMapping("/")
  fun health(): String = "OK"

  @GetMapping("/")
  fun dbHealth() : String {

  }
}

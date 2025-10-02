package com.ngyu.swiftpay.example

import com.ngyu.swiftpay.core.extension.logger
import org.junit.jupiter.api.Test

class ExampleTest {
  private val log = logger()

  @Test
  fun test() {
    //given
    val logText = "로그 출력 테스트"

    //when
    log.trace(logText)
    log.debug(logText)
    log.info(logText)
    log.warn(logText)
    log.error(logText)

    //then
    //콘솔에서 출력되는 로그 확인
  }
}

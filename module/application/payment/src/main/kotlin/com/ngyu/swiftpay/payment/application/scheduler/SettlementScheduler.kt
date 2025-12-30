package com.ngyu.swiftpay.payment.application.scheduler

import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.payment.application.service.settlement.SettlementSchedulerService
import com.ngyu.swiftpay.payment.application.service.settlement.SettlementService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SettlementScheduler(
  private val settlementSchedulerService: SettlementSchedulerService,
  service: SettlementService,
) {

  private val log = logger()

  @Scheduled(cron = "0 0 9 * * *")
  fun processSettle() {

    log.info("==============================================================")
    log.info("======================= 정산 스케줄 시작 =========================")
    log.info("==============================================================")
    settlementSchedulerService.executeSettle()
  }

}

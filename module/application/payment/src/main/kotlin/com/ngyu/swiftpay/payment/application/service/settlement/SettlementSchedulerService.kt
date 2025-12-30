package com.ngyu.swiftpay.payment.application.service.settlement

import com.ngyu.swiftpay.core.domain.settlement.Settlement
import com.ngyu.swiftpay.core.port.repository.SettlementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class SettlementSchedulerService(
  private val settlementRepository: SettlementRepository
) {
  fun executeSettle() {
    CoroutineScope(Dispatchers.IO).launch {
      val settlements = settlementRepository.findByPendingSettlement()
      if (settlements.isEmpty()) return@launch

      settlements.forEach { settlement ->
        launch {
          settle(settlement)
        }
      }
    }
  }

  suspend fun settle(domain: Settlement) {

  }
}

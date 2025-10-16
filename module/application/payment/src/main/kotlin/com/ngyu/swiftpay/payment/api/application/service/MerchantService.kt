package com.ngyu.swiftpay.payment.api.application.service

import com.ngyu.swiftpay.payment.api.application.usecase.MerchantUseCase

class MerchantService: MerchantUseCase {
  /**
   * 가맹점 등록
   *
   * 상태 : PENDING
   */
  override fun register() {
    TODO("Not yet implemented")
  }

  /**
   * 가맹점 승인 ( 관리자 )
   *
   * 상태 : PENDING → ACTIVE
   */
  override fun approve() {
    TODO("Not yet implemented")
  }
}

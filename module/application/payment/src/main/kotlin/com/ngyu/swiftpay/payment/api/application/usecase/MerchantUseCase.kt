package com.ngyu.swiftpay.payment.api.application.usecase

interface MerchantUseCase {

  /**
   * 가맹점 등록
   *
   * 상태 : PENDING
   */
  fun register()

  /**
   * 가맹점 승인 ( 관리자 )
   *
   * 상태 : PENDING → ACTIVE
   */
  fun approve()
}

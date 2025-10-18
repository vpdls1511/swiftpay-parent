package com.ngyu.swiftpay.payment.application.usecase

import com.ngyu.swiftpay.payment.api.dto.MerchantRegisterReqeust
import com.ngyu.swiftpay.payment.api.dto.PaymentCredentials

interface MerchantUseCase {

  /**
   * 가맹점 등록
   *
   * 상태 : PENDING
   */
  fun register(request: MerchantRegisterReqeust): PaymentCredentials // 개발 단계에서 임시로 바로 승인처리 후 api Key 응답해줄 수 있도록..

  /**
   * 가맹점 승인 ( 관리자 )
   *
   * 상태 : PENDING → ACTIVE
   */
  fun approve(merchantId: String): PaymentCredentials
}

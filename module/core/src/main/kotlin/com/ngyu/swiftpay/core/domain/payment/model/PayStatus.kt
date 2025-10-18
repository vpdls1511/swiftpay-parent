package com.ngyu.swiftpay.core.domain.payment.model

enum class PayStatus {
  PENDING,      // 결제 대기 (생성)
  IN_PROGRESS,  // 결제 진행중
  SUCCEEDED,    // 성공
  CANCELLED,    // 취소
  FAILED        // 실패
}

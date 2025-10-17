package com.ngyu.swiftpay.payment.api.application.usecase

import com.ngyu.swiftpay.payment.api.dto.OrderCreateRequestDto
import com.ngyu.swiftpay.payment.api.dto.OrderCreateResponseDto

interface PaymentUseCase {
  fun readyOrder(request: OrderCreateRequestDto): OrderCreateResponseDto
}

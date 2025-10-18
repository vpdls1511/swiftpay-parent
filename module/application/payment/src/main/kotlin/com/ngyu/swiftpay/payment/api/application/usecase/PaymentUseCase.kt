package com.ngyu.swiftpay.payment.api.application.usecase

import com.ngyu.swiftpay.payment.api.dto.OrderCreateRequestDto
import com.ngyu.swiftpay.payment.api.dto.OrderCreateResponseDto
import com.ngyu.swiftpay.payment.api.dto.PaymentRequestDto
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto

interface PaymentUseCase {
  fun readyOrder(request: OrderCreateRequestDto): OrderCreateResponseDto

  fun processing(request: PaymentRequestDto): PaymentResponseDto
}

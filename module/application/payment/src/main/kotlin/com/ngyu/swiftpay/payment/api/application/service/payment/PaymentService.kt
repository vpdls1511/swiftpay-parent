package com.ngyu.swiftpay.payment.api.application.service.payment

import com.ngyu.swiftpay.payment.api.application.usecase.PaymentUseCase
import com.ngyu.swiftpay.payment.api.dto.OrderCreateRequestDto
import com.ngyu.swiftpay.payment.api.dto.OrderCreateResponseDto
import org.springframework.stereotype.Service

@Service
class PaymentService: PaymentUseCase {
  override fun readyOrder(request: OrderCreateRequestDto): OrderCreateResponseDto {
    val domain = request.toDomain()

    return OrderCreateResponseDto.fromDomain(domain)
  }
}

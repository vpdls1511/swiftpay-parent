package com.ngyu.swiftpay.payment.api.application.service.payment

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.payment.api.application.strategy.PaymentStrategyFactory
import com.ngyu.swiftpay.payment.api.application.usecase.PaymentUseCase
import com.ngyu.swiftpay.payment.api.dto.OrderCreateRequestDto
import com.ngyu.swiftpay.payment.api.dto.OrderCreateResponseDto
import com.ngyu.swiftpay.payment.api.dto.PaymentRequestDto
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto
import org.springframework.stereotype.Service

@Service
class PaymentService(
  private val paymentStrategyFactory: PaymentStrategyFactory
) : PaymentUseCase {
  override fun readyOrder(request: OrderCreateRequestDto): OrderCreateResponseDto {
    val domain = request.toDomain()

    return OrderCreateResponseDto.fromDomain(domain)
  }

  override fun processing(request: PaymentRequestDto): PaymentResponseDto {
    val domain = this.savePayment(request)
    val strategy = paymentStrategyFactory.getStrategy(domain)

    val result = if (strategy.shouldAsyncProcessing(domain)) {
      this.processAsync()
    } else {
      this.processSync()
    }

    return PaymentResponseDto.fromDomain(domain)
  }

  private fun savePayment(request: PaymentRequestDto): Payment {
    val domain = request.toDomain()
    val updateDomain = domain.inProgress()
    // TODO("추후 Repository 연결 시 주석 삭제해야함")
    return updateDomain
  }

  private fun processAsync() {

  }

  private fun processSync() {

  }
}

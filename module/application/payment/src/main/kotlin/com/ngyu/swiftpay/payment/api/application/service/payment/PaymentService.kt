package com.ngyu.swiftpay.payment.api.application.service.payment

import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.domain.payment.PaymentRepository
import com.ngyu.swiftpay.payment.api.application.strategy.PaymentStrategyFactory
import com.ngyu.swiftpay.payment.api.application.usecase.PaymentUseCase
import com.ngyu.swiftpay.payment.api.dto.OrderCreateRequestDto
import com.ngyu.swiftpay.payment.api.dto.OrderCreateResponseDto
import com.ngyu.swiftpay.payment.api.dto.PaymentRequestDto
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PaymentService(
  private val paymentStrategyFactory: PaymentStrategyFactory,
  private val paymentRepository: PaymentRepository,
) : PaymentUseCase {
  override fun readyOrder(request: OrderCreateRequestDto): OrderCreateResponseDto {
    val domain = request.toDomain()

    return OrderCreateResponseDto.fromDomain(domain)
  }

  @Transactional
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
    paymentRepository.save(updateDomain)

    return updateDomain
  }

  private fun processAsync() {

  }

  private fun processSync() {

  }
}

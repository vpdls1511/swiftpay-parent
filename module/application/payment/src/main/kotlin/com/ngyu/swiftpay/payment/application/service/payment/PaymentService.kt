package com.ngyu.swiftpay.payment.application.service.payment

import com.ngyu.swiftpay.core.domain.payment.model.Payment
import com.ngyu.swiftpay.core.domain.payment.port.PaymentRepository
import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.payment.api.dto.OrderCreateRequestDto
import com.ngyu.swiftpay.payment.api.dto.OrderCreateResponseDto
import com.ngyu.swiftpay.payment.api.dto.PaymentRequestDto
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto
import com.ngyu.swiftpay.payment.application.strategy.PaymentStrategyFactory
import com.ngyu.swiftpay.payment.application.usecase.PaymentUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PaymentService(
  private val paymentStrategyFactory: PaymentStrategyFactory,
  private val paymentRepository: PaymentRepository,
) : PaymentUseCase {

  private val log = logger()

  override fun readyOrder(request: OrderCreateRequestDto): OrderCreateResponseDto {
    log.info("주문서 생성 시작 | merchantId=${request.merchantId}, orderName=${request.orderName}, amount=${request.totalAmount}")
    val domain = request.toDomain()

    log.info("주문서 생성 완료 | orderId=${domain.orderId}, merchantId=${domain.merchantId}")
    return OrderCreateResponseDto.fromDomain(domain)
  }

  @Transactional
  override fun processing(request: PaymentRequestDto): PaymentResponseDto {
    log.info("결제 처리 시작 | orderId=${request.orderId}, merchantId=${request.merchantId}, method=${request.method}, amount=${request.amount}")
    val domain = this.savePayment(request)
    log.info("결제 정보 저장 완료 :: orderId = ${request.orderId} , paymentId = ${domain.id}")

    val strategy = paymentStrategyFactory.getStrategy(domain)
    val shouldAsyncProcessing = strategy.shouldAsyncProcessing(domain)
    log.info("결제 전략 선택 | paymentId=${domain.id}, strategy=${strategy.getStrategyName()}, isAsync=${shouldAsyncProcessing}")

    // TODO  - 아직 각 전략의 내부 서비스를 완성하지 않은 단계. 우선, 도메인 생성 후 DB 저장까지만.

    val result = if (shouldAsyncProcessing) {
      log.info("비동기 결제 시작 | paymentId=${domain.id}")
      this.processAsync()
    } else {
      log.info("동기 결제 시작 | paymentId=${domain.id}")
      this.processSync()
    }

    return PaymentResponseDto.fromDomain(domain)
  }

  private fun savePayment(request: PaymentRequestDto): Payment {
    log.debug("결제 도메인 생성 | orderId=${request.orderId}")

    val domain = request.toDomain()
    log.info("결제 상태 변경 | paymentId = ${domain.id} , status: PENDING -> IN_PROGRESS")

    val updateDomain = domain.inProgress()
    paymentRepository.save(updateDomain)
    log.info("결제 정보 저장 완료 | paymentId = ${domain.id}, status=${domain.status}")

    return updateDomain
  }

  private fun processAsync() {

  }

  private fun processSync() {

  }
}

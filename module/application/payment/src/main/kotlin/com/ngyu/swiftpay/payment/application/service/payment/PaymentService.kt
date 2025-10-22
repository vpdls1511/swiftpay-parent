package com.ngyu.swiftpay.payment.application.service.payment

import com.ngyu.swiftpay.core.domain.order.OrderRepository
import com.ngyu.swiftpay.core.domain.payment.model.Payment
import com.ngyu.swiftpay.core.domain.payment.port.PaymentRepository
import com.ngyu.swiftpay.core.logger.logger
import com.ngyu.swiftpay.payment.api.dto.OrderCreateRequestDto
import com.ngyu.swiftpay.payment.api.dto.OrderCreateResponseDto
import com.ngyu.swiftpay.payment.api.dto.PaymentRequestDto
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto
import com.ngyu.swiftpay.payment.application.service.EscrowService
import com.ngyu.swiftpay.payment.application.strategy.PaymentStrategyFactory
import com.ngyu.swiftpay.payment.application.usecase.PaymentUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PaymentService(
  private val paymentStrategyFactory: PaymentStrategyFactory,
  private val orderRepository: OrderRepository,
  private val paymentRepository: PaymentRepository,
  private val escrowService: EscrowService,
) : PaymentUseCase {

  private val log = logger()

  override fun readyOrder(request: OrderCreateRequestDto): OrderCreateResponseDto {
    log.info("주문서 생성 시작 | merchantId=${request.merchantId}, orderName=${request.orderName}, amount=${request.totalAmount}")
    val domain = request.toDomain()
    val savedDomain = orderRepository.save(domain)

    log.info("주문서 생성 완료 | orderId=${savedDomain.orderId}, merchantId=${savedDomain.merchantId}")
    return OrderCreateResponseDto.fromDomain(savedDomain)
  }

  @Transactional
  override fun processing(request: PaymentRequestDto): PaymentResponseDto {
    log.info("결제 처리 시작 | orderId=${request.orderId}, merchantId=${request.merchantId}, method=${request.method}, amount=${request.amount}")
    val domain = this.savePayment(request)
    log.info("결제 정보 저장 완료 :: orderId = ${request.orderId} , paymentId = ${domain.paymentId}")

    // TODO  - 아직 각 전략의 내부 서비스를 완성하지 않은 단계. 우선, 도메인 생성 후 DB 저장까지만.
    // TODO - 어느정도 결제 흐름 흘러가면, 그 후에 전략패턴으로 카드/계좌이체 나누자..
    val result = this.processPayment(domain)

    return PaymentResponseDto.fromDomain(result)
  }

  private fun savePayment(request: PaymentRequestDto): Payment {
    val domain = request.toDomain()
    val updateDomain = domain.inProgress()
    paymentRepository.save(updateDomain)
    log.info("결제 정보 저장 완료 | paymentId = ${domain.paymentId}, status=${domain.status}")

    return updateDomain
  }

  private fun processPayment(payment: Payment): Payment {
    return try {
      escrowService.hold(payment)
      log.info("에스크로 예치 성공 | paymentId=${payment.paymentId}")

      val successPayment = payment.success()
      paymentRepository.save(successPayment)

    } catch (e: Exception) {
      log.error("결제 실패 | paymentId=${payment.paymentId}", e)

      val failedPayment = payment.failed(e.message ?: "결제 처리 중 오류")
      paymentRepository.save(failedPayment)
    }
  }

}

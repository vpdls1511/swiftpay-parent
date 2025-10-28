package com.ngyu.swiftpay.payment.application.service.payment

import com.ngyu.swiftpay.core.common.exception.PaymentProcessException
import com.ngyu.swiftpay.core.common.logger.logger
import com.ngyu.swiftpay.core.domain.payment.Payment
import com.ngyu.swiftpay.core.port.PaymentRepository
import com.ngyu.swiftpay.core.port.SequenceGenerator
import com.ngyu.swiftpay.payment.api.dto.PaymentRequestDto
import com.ngyu.swiftpay.payment.api.dto.PaymentResponseDto
import com.ngyu.swiftpay.payment.application.service.escrow.EscrowService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PaymentService(
  // TODO - 아직 각 전략의 내부 서비스를 완성하지 않은 단계. 우선, 도메인 생성 후 DB 저장까지만.
  // private val paymentStrategyFactory: PaymentStrategyFactory,
  private val paymentRepository: PaymentRepository,
  private val escrowService: EscrowService,
  private val sequenceGenerator: SequenceGenerator
) {

  private val log = logger()

  @Transactional
  fun processing(request: PaymentRequestDto): PaymentResponseDto {
    log.info("결제 처리 시작 | orderId=${request.orderId}, merchantId=${request.merchantId}, method=${request.method}, amount=${request.amount}")
    validatePaymentRequest(request)

    val payment = createPayment(request)
    val processed = processPayment(payment)
    val saved = paymentRepository.save(processed)

    log.info("결제 정보 저장 완료 :: orderId = ${request.orderId} , paymentId = ${saved.paymentId}")

    return PaymentResponseDto.fromDomain(saved)
  }

  private fun validatePaymentRequest(request: PaymentRequestDto) {
    // TODO - pending -> progress 결제 요청 자체의 유효성을 검사해야한다..
    // 검증 중 오류가 발생시 throw
  }

  private fun createPayment(request: PaymentRequestDto): Payment {
    val paymentSeq = sequenceGenerator.nextPaymentId()
    val paymentId = Payment.createPaymentId(paymentSeq)
    return request.toDomain(paymentSeq, paymentId).inProgress()
  }

  private fun processPayment(payment: Payment): Payment {
    return try {
      escrowService.hold(payment)
      log.info("에스크로 예치 성공 | paymentId=${payment.paymentId}")

      payment.success()
    } catch (e: Exception) {
      log.error("결제 실패 | paymentId=${payment.paymentId}", e)
      val failure = payment.failed(e.message ?: "결제 처리 중 오류")
      paymentRepository.save(failure)
      throw PaymentProcessException()
    }
  }

}
